package com.github.loki.afro.metallum.core.parser.site;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.core.parser.site.helper.band.BandLinkParser;
import com.github.loki.afro.metallum.core.parser.site.helper.band.DiscParser;
import com.github.loki.afro.metallum.core.parser.site.helper.band.MemberParser;
import com.github.loki.afro.metallum.core.parser.site.helper.band.SimilarArtistsParser;
import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Link;
import com.github.loki.afro.metallum.entity.YearRange;
import com.github.loki.afro.metallum.entity.partials.PartialImage;
import com.github.loki.afro.metallum.entity.partials.PartialLabel;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BandSiteParser extends AbstractSiteParser<Band> {
    private final boolean loadSimilarArtists;
    private final boolean loadReadMore;
    private static final Logger logger = LoggerFactory.getLogger(BandSiteParser.class);
    private static final Pattern periodMatcher = Pattern.compile("(\\d\\d\\d\\d|\\?)(?:-(\\d\\d\\d\\d|present|\\?))?(?:\\s\\(as\\s(.+)\\))?");

    public BandSiteParser(final long entityId, final boolean loadSimilarArtists, final boolean loadLinks, final boolean loadReadMore) {
        super(entityId, loadLinks);
        this.loadSimilarArtists = loadSimilarArtists;
        this.loadReadMore = loadReadMore;
    }

    @Override
    public final Band parse() {
        Band band = new Band(entityId, parseBandName());

        parseLeftHtmlPart(band);
        parseRightHtmlPart(band);
        parseYearsActive(band);

        band = parseBandImages(band);

        band.setInfo(parseInfo());
        band.setDiscs(parseDiscography());
        parseMember(band);
        band.setSimilarArtists(parseSimilarArtists());
        band.addLinks(parseLinks());
        band = parseModifications(band);
        logger.debug("parsed Entity: " + band);
        return band;
    }

    private final Band parseBandImages(final Band band) {
        final String logoUrl = parseLogoUrl();
        band.setLogo(parseBandLogo(logoUrl));
        final String photoUrl = parsePhotoUrl();
        band.setPhoto(parseBandPhoto(photoUrl));
        return band;
    }

    private String parseLogoUrl() {
        return parseImageURL("band_name_img");
    }

    private String parsePhotoUrl() {
        return parseImageURL("band_img");
    }

    private void parseRightHtmlPart(final Band band) {
        Element secondPart = this.doc.select("dl[class]").get(1);
        if (secondPart.hasClass("float_right")) {
            Elements valueElements = secondPart.getElementsByTag("dd");
            band.setGenre(valueElements.get(0).text().trim());
            band.setLyricalThemes(valueElements.get(1).text().trim());
            band.setLabel(parseCurrentLabel(valueElements.get(2)));
        }
    }

    /**
     * This Method set the Country Province, Status and the year.
     */
    private void parseLeftHtmlPart(final Band band) {
        Element secondPart = this.doc.select("dl[class]").get(0);
        if (secondPart.hasClass("float_left")) {
            Elements valueElements = secondPart.getElementsByTag("dd");
            band.setCountry(Country.ofMetallumDisplayName(valueElements.get(0).text()));
            band.setProvince(valueElements.get(1).text().trim());
            band.setStatus(BandStatus.getTypeBandStatusForString(valueElements.get(2).text()));
            band.setYearFormedIn(parseYearOfCreation(valueElements.get(3).text()));
        }
    }

    private void parseYearsActive(Band band) {
        Elements select = this.doc.select("#band_stats > dl.clear > dd:nth-child(2)");
        if (!select.isEmpty()) {
            Element yearsActiveElement = select.get(0);
            Map<String, Long> referencedBands = new HashMap<>();
            for (Element href : yearsActiveElement.getElementsByTag("a")) {
                String urlStr = href.attr("href");
                String bandId = urlStr.substring(urlStr.lastIndexOf("/") + 1);
                String bandName = href.text();
                referencedBands.put(bandName, Long.parseLong(bandId));
            }
            String yearsActiveFullText = yearsActiveElement.text();
            if ("N/A".contains(yearsActiveFullText)) {
                band.setYearsActive(new TreeSet<>());
            } else {
                band.setYearsActive(getYearsActive(band, referencedBands, yearsActiveFullText));
            }

        } else {
            band.setYearsActive(new TreeSet<>());
        }
    }

    private TreeSet<YearRange> getYearsActive(Band band, Map<String, Long> referencedBands, String yearsActiveFullText) {
        TreeSet<YearRange> yearsActive = new TreeSet<>();
        for (String period : yearsActiveFullText.split(",\\s?")) {
            Matcher matcher = periodMatcher.matcher(period);
            if (matcher.matches()) {
                String start = matcher.group(1);
                String end = matcher.group(2);
                if (end == null) {
                    // 1989-1990 (as Ulceration), 1990 (as Fear the Factory), 1990-2002
                    // only in 1990 they called themselves "Fear the Factory" therefore the period also ends in 1990
                    end = start;
                }
                String bandName = matcher.group(3);

                final Long bandId;
                if (bandName != null) {
                    bandId = referencedBands.get(bandName);
                } else {
                    bandId = band.getId();
                    bandName = band.getName();
                }

                YearRange.Year startYear = yearFromString(start);
                YearRange.Year endYear = yearFromString(end);

                yearsActive.add(YearRange.of(startYear, endYear, bandName, bandId));
            } else {
                logger.error("Unknown period pattern: {}", period);
            }
        }
        return yearsActive;
    }

    private YearRange.Year yearFromString(String strYear) {
        if ("?".equals(strYear)) {
            return YearRange.Year.unknown();
        } else if ("present".equals(strYear)) {
            return YearRange.Year.present();
        } else {
            return YearRange.Year.of(Integer.parseInt(strYear));
        }
    }

    private final String parseBandName() {
        Element bandNameElement = this.doc.getElementsByClass("band_name").first();
        return bandNameElement.text();
    }

    private final int parseYearOfCreation(final String firstPartHtml) {
        if (firstPartHtml.contains("N/A")) {
            return 0;
        }
        return Integer.parseInt(firstPartHtml);
    }

    private final PartialLabel parseCurrentLabel(final Element labelElement) {
        // id
        String labelId;
        String labelElementText = labelElement.html();
        if (!labelElement.text().contains("Unsigned/independent") && !labelElement.text().contains("Unknown")) {
            labelId = labelElementText.substring(labelElementText.indexOf("/labels/") + 8);
            labelId = labelId.substring(labelId.indexOf("/") + 1, labelId.indexOf("\">"));
        } else {
            labelId = "0";
        }

        return new PartialLabel(Long.parseLong(labelId), labelElement.text().trim());

    }

    private final String parseInfo() {
        Element bandComment = this.doc.getElementsByClass("band_comment").get(0);
        String info = MetallumUtil.htmlToPlainText(bandComment.html()).replaceAll(" Read more$", "");

        if (this.loadReadMore && !this.doc.getElementsByClass("btn_read_more").isEmpty()) {
            try {
                final String downloadedReadMore = Downloader.getHTML(MetallumURL.assembleMoreInfoURL(this.entityId));
                return MetallumUtil.htmlToPlainText(downloadedReadMore);
            } catch (MetallumException e) {
                logger.error("error in parsing additional information for band: " + this.entityId, e);
            }
        }
        return info;
    }

    // we do not specify complete /main/live/demo/misc -> BandGeneric
    private final List<Band.PartialDisc> parseDiscography() {
        try {
            final DiscParser discParser = new DiscParser(this.entityId);
            return discParser.parse();
        } catch (final MetallumException e) {
            throw new MetallumException("error in parsing the discography for band: " + this.entityId, e);
        }
    }

    private void parseMember(final Band band) {
        final MemberParser memberParser = new MemberParser();
        memberParser.parse(this.doc);
        // split by cat
        band.setCurrentMembers(memberParser.getCurrentLineup());
        band.setPastMembers(memberParser.getPastLineup());
        band.setCurrentLiveMembers(memberParser.getCurrentLiveLineup());
        band.setPastLiveMembers(memberParser.getPastLiveLineupList());
    }

    private final Map<Integer, List<Band.SimilarBand>> parseSimilarArtists() {
        if (this.loadSimilarArtists) {
            try {
                final SimilarArtistsParser sap = new SimilarArtistsParser(this.entityId);
                return sap.parse();
            } catch (final ExecutionException e) {
                throw new MetallumException("error in parsing similar Artists for band: " + this.entityId, e);
            }
        }
        return new HashMap<>();
    }

    private final Link[] parseLinks() {
        if (this.loadLinks) {
            try {
                final BandLinkParser parser = new BandLinkParser(this.entityId);
                return parser.parse();
            } catch (final ExecutionException e) {
                throw new MetallumException("error in parsing similar Artists for band: " + this.entityId, e);
            }
        } else {
            return new Link[0];
        }
    }

    private PartialImage parseBandLogo(final String imageUrl) {
        if (imageUrl != null) {
            try {
                return new PartialImage(imageUrl);
            } catch (final MetallumException e) {
                throw new MetallumException("Exception while downloading an image from \"" + imageUrl + "\" ," + this.entityId, e);
            }
        } else {
            return null;
        }
    }

    private PartialImage parseBandPhoto(final String photoUrl) {
        if (photoUrl != null) {
            try {
                return new PartialImage(photoUrl);
            } catch (MetallumException e) {
                throw new MetallumException("Exception while downloading an image from \"" + photoUrl + "\" ," + this.entityId, e);
            }
        } else {
            return null;
        }
    }

    @Override
    protected final String getSiteURL() {
        return MetallumURL.assembleBandURL(this.entityId);
    }
}
