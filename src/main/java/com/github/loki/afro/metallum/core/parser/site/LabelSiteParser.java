package com.github.loki.afro.metallum.core.parser.site;

import com.github.loki.afro.metallum.core.parser.site.helper.LinkParser;
import com.github.loki.afro.metallum.core.parser.site.helper.label.CurrentRosterParser;
import com.github.loki.afro.metallum.core.parser.site.helper.label.PastRosterParser;
import com.github.loki.afro.metallum.core.parser.site.helper.label.ReleaseParser;
import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.entity.Link;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.LabelStatus;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LabelSiteParser extends AbstractSiteParser<Label> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LabelSiteParser.class);

    /**
     * There are ways to sort the return values of the rooster and release parsers.<br>
     * This Enumeration gives you an Idea how you can Sort that data.
     *
     * @author Zarathustra
     */
    public enum PARSE_STYLE {
        /**
         * If there is nothing to parse.
         */
        NONE(-1),
        /**
         * If you want to sort the result by band alphabetical. (suggested)
         */
        BAND_SEARCH_MODE(0),
        /**
         * If you want to sort the result by Genre alphabetical.
         */
        GENRE_SEARCH_MODE(1),
        /**
         * If you want to sort the result by Country alphabetical.
         */
        COUNTRY_SEARCH_MODE(2);

        private final int searchNumber;

        PARSE_STYLE(final int searchNumber) {
            this.searchNumber = searchNumber;
        }

        public int asSearchNumber() {
            return this.searchNumber;
        }
    }

    private final PARSE_STYLE loadCurrentRooster;
    private final PARSE_STYLE loadPastRooster;
    private final PARSE_STYLE loadReleases;

    /**
     * The SiteParser for the Label!<br>
     * <br>
     * A good example for a Label with all tags and so on: <br>
     * @see <a href="http://www.metal-archives.com/labels/Metal_Blade_Records/3"></a>.<br>
     * <br>
     * <b>The last 3 parameter...</b>
     * <br>
     * currentRooser, pastRooster and releases are PARSE_STYLES.<br>
     * They can change the behaviour how metal-archives present their data.<br>
     * You should use PARSE_STYLE.BAND_SEARCH_MODE if care about the data and don't understand the
     * documentation. <br>
     * By default the 3 Fields are disabled (PARSE_STYLE.NONE)
     *
     * @param loadImages     if you want are interested in the Label-logo.
     * @param currentRooster If you care about the current Bands that are used by this Label.
     * @param pastRooster    If you care about the past Bands that were used by this Label.
     * @param releases       If you care about the releases that this Label published.
     * @throws ExecutionException if there is any error occurred
     */
    public LabelSiteParser(final Label label, final boolean loadImages, final boolean loadLinks, final PARSE_STYLE currentRooster, final PARSE_STYLE pastRooster, final PARSE_STYLE releases) throws ExecutionException {
        super(label, loadImages, loadLinks);
        this.loadCurrentRooster = currentRooster;
        this.loadPastRooster = pastRooster;
        this.loadReleases = releases;
    }

    @Override
    public final Label parse() {
        Label label = new Label(this.entity.getId());
        label.setName(parseLabelName());

        // upper part
        label = parseLeftSide(label);
        label = parseRightSide(label);

        // middle part
        label = parseContactData(label);

        // lower part
        label.setCurrentRoster(parseCurrentRoster());
        label.setPastRoster(parsePastRoster());
        label.setReleases(parseReleases());

        label.addLink(parseLinks());
        label.setAdditionalNotes(parseAdditionalNotes());
        final String logoUrl = parseLogoUrl();
        label.setLogoUrl(logoUrl);
        label.setLogo(parseLabelLogo(logoUrl));
        label = parseModifications(label);
        return label;
    }

    private Label parseRightSide(final Label label) {
        String newHtml = this.html.substring(this.html.indexOf("</dl>") + 5);
        String[] upperRightPart = newHtml.substring(0, newHtml.indexOf("</dl>")).split("<dd>");
        label.setStatus(parseLabelStatus(upperRightPart[1]));
        label.setSpecialisation(parseSpecialisedIn(upperRightPart[2]));
        label.setFoundingDate(parseFoundingDate(upperRightPart[3]));
        label.setSubLabels(parseSubLabels(upperRightPart[4]));
        if (upperRightPart[3].contains("<dt>Parent label:</dt>")) {
            label.setParentLabel(parseParentLabel(upperRightPart[4]));
        }
        label.setOnlineShopping(parseHasOnlineShopping(upperRightPart[upperRightPart.length - 1]));
        return label;
    }

    private Label parseLeftSide(final Label label) {
        String[] upperLeftPart = this.html.substring(0, this.html.indexOf("</dl>")).split("<dd");
        label.setAddress(parseAddress(upperLeftPart[1]));
        label.setCountry(parseCountry(upperLeftPart[2]));
        label.setPhoneNumber(parsePhoneNumber(upperLeftPart[3]));
        return label;
    }

    private String parseLabelName() {
        String name = this.html.substring(this.html.indexOf("<h1 class=\"label_name\">") + 22);
        name = name.substring(name.indexOf(">") + 1, name.indexOf("</h1>"));
        return name;
    }

    private Link[] parseLinks() {
        final List<Link> linksFromEntity = this.entity.getLinks();
        if (!linksFromEntity.isEmpty()) {
            Link[] linkArray = new Link[linksFromEntity.size()];
            linksFromEntity.toArray(linkArray);
            return linkArray;
        } else if (this.loadLinks) {
            try {
                final LinkParser parser = new LinkParser(this.entity.getId(), LinkParser.LABEL_PARSER);
                return parser.parse();
            } catch (final ExecutionException e) {
                LOGGER.error("unable to parse label links from " + this.entity, e);
            }
        }
        return new Link[0];

    }

    private String parseAddress(final String upperLeftPart) {
        String address = upperLeftPart.substring(upperLeftPart.indexOf("> ") + 2, upperLeftPart.indexOf("</dd>"));
        address = MetallumUtil.parseHtmlWithLineSeparators(address);
        return address;
    }

    private Country parseCountry(final String upperLeftPart) {
        String countryStr = upperLeftPart.substring(upperLeftPart.indexOf(">") + 1, upperLeftPart.indexOf("</dd>"));
        return Country.getRightCountryForString(countryStr);
    }

    private String parsePhoneNumber(final String upperLeftPart) {
        return upperLeftPart.substring(upperLeftPart.indexOf("> ") + 2, upperLeftPart.indexOf("</dd>")).trim();
    }

    private LabelStatus parseLabelStatus(final String upperRightPart) {
        String status = upperRightPart.substring(upperRightPart.indexOf("\">") + 2, upperRightPart.indexOf("</sp"));
        return LabelStatus.getLabelStatusForString(status);
    }

    private String parseSpecialisedIn(final String upperRightPart) {
        return upperRightPart.substring(1, upperRightPart.indexOf(" </dd>"));
    }

    private String parseFoundingDate(final String upperRightPart) {
        return upperRightPart.substring(1, upperRightPart.indexOf(" </dd>"));
    }

    private Label parseParentLabel(final String upperRightPart) {
        String labelName = upperRightPart.substring(upperRightPart.indexOf("\">") + 2, upperRightPart.indexOf("</a></dd>"));
        String labelId = upperRightPart.substring(0, upperRightPart.indexOf("\">" + labelName));
        labelId = labelId.substring(labelId.lastIndexOf("/") + 1);
        return new Label(Long.parseLong(labelId), labelName);
    }

    private List<Label> parseSubLabels(final String upperRightPart) {
        List<Label> labelList = new ArrayList<>();
        // must and with </dd> and at least 9 characters to match
        if (!upperRightPart.trim().matches(".{9,}?[</dd>]$")) {
            return labelList;
        }
        final String[] labelLinks = upperRightPart.split(",");
        for (final String labelLink : labelLinks) {
            // prepare, to remove Online Shopping if it appears
            String parseAbleString = labelLink.replaceAll("(?imx)</dd>.*", "").trim();
            // name
            String labelName = Jsoup.parse(parseAbleString).text();
            // id
            String labelId = parseAbleString.substring(0, parseAbleString.length() - (labelName.length() + 6));
            labelId = labelId.substring(labelId.lastIndexOf("/") + 1);
            labelList.add(new Label(Long.parseLong(labelId), labelName));
        }
        return labelList;
    }

    private boolean parseHasOnlineShopping(final String upperRightPart) {
        String value = Jsoup.parse(upperRightPart).text();
        return value.equalsIgnoreCase("Yes");
    }

    /**
     * Here you'll get all Bands which are currently used by this Label.<br>
     * <br>
     * <b>ONLY if option is set</b>
     *
     * @return a List of Bands, if there are none, you'll get a empty List.
     */
    private List<Band> parseCurrentRoster() {
        List<Band> roster = this.entity.getCurrentRoster();
        if (!roster.isEmpty()) {
            return roster;
        } else if (this.loadCurrentRooster != PARSE_STYLE.NONE) {
            try {
                final CurrentRosterParser parser = new CurrentRosterParser(this.entity.getId(), Byte.MAX_VALUE, true, this.loadCurrentRooster);
                return new ArrayList<>(parser.parse().values());
            } catch (final Exception e) {
                LOGGER.error("Unable to parse current roster", e);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Can return a Map with the Band entry (as Key) where Band.getId () = 0 & Band.getName(Various
     * Artists) <br>
     * You'll get this when the Label also releases a SplitDisc., in the List&lt;Disc&gt; of that band,<br>
     * which is the SplitDisc you'll get the real Bands.<br>
     * <br>
     * <b>ONLY if option is set</b>
     *
     * @return the Map with Band as Key and Integer as Value representing the quantum of releases<br>
     * If there are none you'll get a empty HashMap.
     */
    private Map<Band, Integer> parsePastRoster() {
        final Map<Band, Integer> pastRoster = this.entity.getPastRoster();
        if (!pastRoster.isEmpty()) {
            return pastRoster;
        } else if (this.loadPastRooster != PARSE_STYLE.NONE) {
            // try {
            try {
                return new PastRosterParser(this.entity.getId(), Byte.MAX_VALUE, true, this.loadPastRooster).parse();
            } catch (final Exception e) {
                LOGGER.error("unable to parse past roster with " + this.loadPastRooster + " and " + this.entity, e);
            }
        }
        return new HashMap<>();
    }

    /**
     * You'll get a Map with Band as Key and Disc as Value.<br>
     * <br>
     * To go more into Detail: This method will return all the Discs which are released under this
     * Label<br>
     * mapped to the specific Band<br>
     * <br>
     * <b>ONLY if option is set</b>
     *
     * @return a Map with Band as Key and a List with Discs.
     */
    private Map<Band, List<Disc>> parseReleases() {
        Map<Band, List<Disc>> releases = this.entity.getReleases();
        if (!releases.isEmpty()) {
            return releases;
        } else if (this.loadReleases != PARSE_STYLE.NONE) {
            try {
                return new ReleaseParser(this.entity.getId(), Byte.MAX_VALUE, true, this.loadReleases).parse();
            } catch (final Exception e) {
                LOGGER.error("unable to parse label releases with " + this.loadReleases + " and " + this.entity, e);
            }
        }
        return new HashMap<>();
    }

    private String parseAdditionalNotes() {
        String additionalNotes = "";
        if (this.html.contains("<div id=\"label_notes")) {
            additionalNotes = this.html.substring(this.html.indexOf("<div id=\"label_notes"), this.html.indexOf("<div id=\"auditTrail"));
            additionalNotes = additionalNotes.replaceAll("<.?p>", "<br><br>");
            additionalNotes = MetallumUtil.parseHtmlWithLineSeparators(additionalNotes);
        }
        return additionalNotes;
    }

    private Label parseContactData(final Label label) {
        String contactHtml = this.html.substring(this.html.indexOf("<p id=\"label_contact\">") + 22);
        contactHtml = contactHtml.substring(0, contactHtml.indexOf("</p>"));

        label.setWebSiteURL(parseLabelWebsiteURL(contactHtml));
        label.setEmail(parseLabelEmail(contactHtml));
        return label;
    }

    private Link parseLabelWebsiteURL(final String concatHtml) {
        String htmlString = concatHtml;
        if (htmlString.contains("title=\"Email\"")) {
            htmlString = htmlString.substring(0, htmlString.indexOf("title=\"Email\""));
        }
        String webSitename = Jsoup.parse(htmlString).text().trim();
        if (webSitename.isEmpty()) {
            return new Link();
        }
        Link website = new Link();
        website.setName(webSitename);

        String url = htmlString.substring(htmlString.indexOf("<a href=\"") + 9);
        url = url.substring(0, url.indexOf("\""));
        website.setURL(url);
        return website;
    }

    /**
     * Parses the specific HTML part for the email of the Label.
     *
     * @param htmlPart the specific HTML part from the Contact section
     * @return the email of the Label if available otherwise an empty String.
     */
    private String parseLabelEmail(final String htmlPart) {
        String htmlString = htmlPart;
        if (htmlString.contains("title=\"Email\"")) {
            htmlString = htmlString.substring(htmlString.indexOf("title=\"Email\""));
            String mail = htmlString.substring(0, htmlString.indexOf("</a>"));
            mail = mail.substring(mail.lastIndexOf("\">") + 2);
            // the mail is in this structure "hidden":
            // geil@google.com -> moc\elgoog\\lieg
            mail = new StringBuffer(mail).reverse().toString();
            mail = mail.replaceAll("//", "@");
            mail = mail.replaceAll("/", ".");
            return mail;
        }
        return "";
    }

    private final String parseLogoUrl() {
        String logoUrl = null;
        if (this.html.contains("class=\"label_img\"")) {
            logoUrl = this.html.substring(this.html.indexOf("class=\"label_img\"") + 16);
            logoUrl = logoUrl.substring(logoUrl.indexOf("src=\"") + 5);
            logoUrl = logoUrl.substring(0, logoUrl.indexOf("\""));
        }
        return logoUrl;
    }

    /**
     * If loadImage is true this method tries to download the Label logo.
     *
     * @return null if loadImage is false or if there is no artwork.
     */
    private final BufferedImage parseLabelLogo(final String logoUrl) {
        if (this.loadImage && logoUrl != null) {
            try {
                return Downloader.getImage(logoUrl);
            } catch (final ExecutionException e) {
                LOGGER.error("Exception while downloading an image from \"" + logoUrl + "\" ," + this.entity, e);
            }
        }
        return null;
    }

    @Override
    protected final String getSiteURL() {
        return MetallumURL.assembleLabelURL(this.entity.getId());
    }

}
