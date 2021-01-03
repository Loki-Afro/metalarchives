package com.github.loki.afro.metallum.core.parser.site;

import com.github.loki.afro.metallum.core.parser.site.helper.ReviewParser;
import com.github.loki.afro.metallum.core.parser.site.helper.band.BandLinkParser;
import com.github.loki.afro.metallum.core.parser.site.helper.band.DiscParser;
import com.github.loki.afro.metallum.core.parser.site.helper.band.MemberParser;
import com.github.loki.afro.metallum.core.parser.site.helper.band.SimilarArtistsParser;
import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.*;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BandSiteParser extends AbstractSiteParser<Band> {
    private final boolean loadReviews;
    private final boolean loadSimilarArtists;
    private final boolean loadReadMore;
    private static final Logger logger = LoggerFactory.getLogger(BandSiteParser.class);
    private static final Pattern periodMatcher = Pattern.compile("(\\d\\d\\d\\d|\\?)(?:-(\\d\\d\\d\\d|present|\\?))?(?:\\s\\(as\\s(.+)\\))?");

    public BandSiteParser(final Band band, final boolean loadImages, final boolean loadReviews, final boolean loadSimilarArtists, final boolean loadLinks, final boolean loadReadMore) throws ExecutionException {
        super(band, loadImages, loadLinks);
        this.loadReviews = loadReviews;
        this.loadSimilarArtists = loadSimilarArtists;
        this.loadReadMore = loadReadMore;
    }

    @Override
    public final Band parse() {
        Band band = new Band(this.entity.getId());
        band.setName(parseBandName());

        parseLeftHtmlPart(band);
        parseRightHtmlPart(band);
        parseYearsActive(band);

        band = parseBandImages(band);

        band.setInfo(parseInfo());
        for (final Disc disc : parseDiscography()) {
            disc.setBand(band);
            band.addToDiscography(disc);
        }
        parseMember(band);
        band.addToReviews(parseReviews(band));
        band.setSimilarArtists(parseSimilarArtists());
        band.addLinks(parseLinks());
        band = parseModifications(band);
        logger.debug("parsed Entity: " + band);
        return band;
    }

    private final Band parseBandImages(final Band band) {
        final String logoUrl = parseLogoUrl();
        band.setLogoUrl(logoUrl);
        band.setLogo(parseBandLogo(logoUrl));
        final String photoUrl = parsePhotoUrl();
        band.setPhotoUrl(photoUrl);
        band.setPhoto(parseBandPhoto(photoUrl));
        return band;
    }

    private String parseLogoUrl() {
        return parseImageURL(this.doc, "band_name_img");
    }

    private String parsePhotoUrl() {
        return parseImageURL(this.doc, "band_img");
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

    private final Label parseCurrentLabel(final Element labelElement) {
        // id
        String labelId;
        String labelElementText = labelElement.html();
        if (!labelElement.text().contains("Unsigned/independent") && !labelElement.text().contains("Unknown")) {
            labelId = labelElementText.substring(labelElementText.indexOf("/labels/") + 8);
            labelId = labelId.substring(labelId.indexOf("/") + 1, labelId.indexOf("\">"));
        } else {
            labelId = "0";
        }
        final Label label = new Label(Long.parseLong(labelId));

        // name
        label.setName(labelElement.text().trim());
        return label;

    }

    private final String parseInfo() {
        Element bandComment = this.doc.getElementsByClass("band_comment").get(0);
        String info = MetallumUtil.parseHtmlWithLineSeparators(bandComment.html()).replaceAll(" Read more$", "");

        if (this.entity.getInfo().length() > info.length()) {
            return this.entity.getInfo();
        } else if (this.loadReadMore && !this.doc.getElementsByClass("btn_read_more").isEmpty()) {
            try {
                final String downloadedReadMore = Downloader.getHTML(MetallumURL.assembleMoreInfoURL(this.entity.getId()));
                return MetallumUtil.parseHtmlWithLineSeparators(downloadedReadMore);
            } catch (ExecutionException e) {
                logger.error("error in parsing additional information for band: " + this.entity, e);
            }
        }
        return info;
    }

    // we do not specify complete /main/live/demo/misc -> BandGeneric
    private final Disc[] parseDiscography() {
        final List<Disc> discs = this.entity.getDiscs();
        if (discs.isEmpty()) {
            try {
                final DiscParser discParser = new DiscParser(this.entity.getId());
                return discParser.parse();
            } catch (final ExecutionException e) {
                logger.error("error in parsing the discography for band: " + this.entity, e);
            }
        }
        final Disc[] discArray = new Disc[discs.size()];
        return discs.toArray(discArray);
    }

    private void parseMember(final Band band) {
        final MemberParser memberParser = new MemberParser();
        memberParser.parse(this.html);
        // split by cat
        band.setCurrentLineup(memberParser.getCurrentLineup());
        band.setPastLineup(memberParser.getPastLineup());
        band.setLiveLineup(memberParser.getLiveLineup());
        band.setLastKnownLineup(memberParser.getLastKnownLineup());
    }

    private final Review[] parseReviews(final Band band) {
        final List<Review> reviews = this.entity.getReviews();
        if (!reviews.isEmpty()) {
            final Review[] reviewArr = new Review[reviews.size()];
            return reviews.toArray(reviewArr);
        } else if (this.loadReviews) {
            final List<Review> parsedReviewList = new ArrayList<>();
            for (final Disc disc : band.getDiscs()) {
                try {
                    final ReviewParser parser = new ReviewParser(disc.getId());
                    for (final Review review : parser.parse()) {
                        review.setDisc(disc);
                        parsedReviewList.add(review);
                    }
                } catch (final ExecutionException e) {
                    logger.error("error in parsing " + Review.class + " for band: " + band, e);
                }
            }
            final Review[] reviewArr = new Review[parsedReviewList.size()];
            return parsedReviewList.toArray(reviewArr);
        } else {
            return new Review[0];
        }

    }

    private final Map<Integer, List<Band>> parseSimilarArtists() {
        final Map<Integer, List<Band>> similarArtists = this.entity.getSimilarArtists();
        if (!similarArtists.isEmpty()) {
            return similarArtists;
        } else if (this.loadSimilarArtists) {
            try {
                final SimilarArtistsParser sap = new SimilarArtistsParser(this.entity.getId());
                return sap.parse();
            } catch (final ExecutionException e) {
                logger.error("error in parsing similar Artists for band: " + this.entity, e);
            }
        }
        return new HashMap<>();
    }

    private final Link[] parseLinks() {
        final List<Link> links = this.entity.getLinks();
        if (!links.isEmpty()) {
            return links.toArray(new Link[links.size()]);
        }
        if (this.loadLinks) {
            try {
                final BandLinkParser parser = new BandLinkParser(this.entity.getId());
                return parser.parse();
            } catch (final ExecutionException e) {
                logger.error("error in parsing " + Link.class + " for band: " + this.entity, e);
            }
        }
        return new Link[0];
    }

    /**
     * If the previous entity, may from cache, has already the band logo,
     * this method will return the BufferedImage of the entity, otherwise if loadImage is true
     * this method with try to get the Image, if it is in the Metal-Archives, via the Downloader.
     *
     * @return null if loadImage is false or if there is no logo.
     */
    private final BufferedImage parseBandLogo(final String imageUrl) {
        BufferedImage imageLogo = this.entity.getLogo();
        if (imageLogo == null && this.loadImage && imageUrl != null) {
            try {
                imageLogo = Downloader.getImage((imageUrl));
            } catch (final ExecutionException e) {
                logger.error("Exception while downloading an image from \"" + imageUrl + "\" ," + this.entity, e);
            }
        }
        // with other words, null
        return imageLogo;
    }

    /**
     * If the previous entity, may from cache, has already the band photo,
     * this method will return the BufferedImage of the entity, otherwise if loadImage is true
     * this method with try to get the Image, if it is in the Metal-Archives, via the Downloader.
     *
     * @return null if loadImage is false or if there is no photo.
     */
    private final BufferedImage parseBandPhoto(final String photoUrl) {
        BufferedImage imagePhoto = this.entity.getPhoto();
        if (imagePhoto == null && this.loadImage && photoUrl != null) {
            try {
                imagePhoto = Downloader.getImage(photoUrl);
            } catch (ExecutionException e) {
                logger.error("Exception while downloading an image from \"" + photoUrl + "\" ," + this.entity, e);
            }
        }
        return imagePhoto;
    }

    @Override
    protected final String getSiteURL() {
        return MetallumURL.assembleBandURL(this.entity.getId());
    }
}
