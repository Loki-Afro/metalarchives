package com.github.loki.afro.metallum.core.parser.site;

import com.github.loki.afro.metallum.core.parser.site.helper.disc.DiscSiteMemberParser;
import com.github.loki.afro.metallum.core.parser.site.helper.disc.DiscSiteTrackParser;
import com.github.loki.afro.metallum.core.util.MetallumUtil;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.entity.partials.PartialImage;
import com.github.loki.afro.metallum.entity.partials.PartialLabel;
import com.github.loki.afro.metallum.entity.partials.PartialReview;
import com.github.loki.afro.metallum.enums.DiscType;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DiscSiteParser extends AbstractSiteParser<Disc> {

    private static final Logger logger = LoggerFactory.getLogger(DiscSiteParser.class);

    /**
     * Creates a new DiscParser, just call parse
     */
    public DiscSiteParser(final long entityId) {
        super(entityId, false);
    }

    @Override
    public Disc parse() {
        Disc disc = new Disc(this.entityId, parseName());
        disc.setDiscType(parseDiscType());
        if (disc.isSplit()) {
            disc.setSplitBands(parseSplitBands());
        } else {
            disc.setBand(parseBand());
        }
        disc.addTracks(parseTracks(disc));

        disc.setArtwork(parseDiscArtwork());
        disc.setLabel(parseLabel());
        disc.setDetails(parseDetails());
        disc.setReleaseDate(parseReleaseDate());
        disc = parseMember(disc);
        disc.setReviews(parseReviewList(disc.getId()));
        disc = parseModifications(disc);
        return disc;
    }

    private String parseDetails() {
        Element notesElement = this.doc.getElementById("album_tabs_notes");
        if (notesElement != null) {
            return MetallumUtil.htmlToPlainText(notesElement.html());
        }
        return "";
    }

    private String parseName() {
        Element albumNameElement = this.doc.getElementsByClass("album_name").first();
        logger.debug("albumName: " + albumNameElement.text());
        return albumNameElement.text();
    }

    private List<Track> parseTracks(final Disc disc) {
        DiscSiteTrackParser trackParser = new DiscSiteTrackParser(this.doc, disc);
        List<Track> tracks = trackParser.parse();
        int discCount = tracks.stream()
                .mapToInt(Track::getDiscNumber)
                .max()
                .orElse(1);
        disc.setDiscCount(discCount);
        return tracks;
    }

    /**
     * Parses the DiscType from the left float thing.
     *
     * @return the parsed DiscType.
     */
    private DiscType parseDiscType() {
        Element leftThing = this.doc.select("dl[class=float_left]").first();
        String discType = leftThing.getElementsByTag("dd").first().text();
        return DiscType.getTypeDiscTypeForString(discType);
    }

    /**
     * Parses the release date, as String, from the left float thing.
     *
     * @return the parsed release date as String.
     */
    private String parseReleaseDate() {
        Element leftThing = this.doc.select("dl[class=float_left]").first();
        return leftThing.getElementsByTag("dd").get(1).text();
    }

    /**
     * Tries to parse the label.
     * If there is none or unsigned/selfreleased,
     * a new Label will be created with id = 0 and name unsigned/selfreleased.
     *
     * @return the Label of the Disc if there is one.
     */
    private PartialLabel parseLabel() {
        Element labelElement = this.doc.select("dl[class=float_right]").first();
        Element labelLink = labelElement.getElementsByTag("dd").first();
        String labelName = labelLink.text();
        String labelIdStr = "0";
//		if there is no link, there is now Label aka unsigned/selfreleased
        Element link = labelLink.getElementsByTag("a").first();
        if (link != null) {
            labelIdStr = link.attr("href");
            labelIdStr = labelIdStr.substring(0, labelIdStr.indexOf("#"));
            labelIdStr = labelIdStr.substring(labelIdStr.lastIndexOf("/") + 1);
            if (labelIdStr.contains("#")) {
                labelIdStr = labelIdStr.substring(0, labelIdStr.indexOf("#"));
            }
        }
        return new PartialLabel(Long.parseLong(labelIdStr), labelName);
    }

    private Disc parseMember(final Disc disc) {
        final DiscSiteMemberParser parser = new DiscSiteMemberParser(this.doc);
        parser.parse();
        disc.setLineup(parser.getLineup());
        disc.setGuestLineup(parser.getGuestLineup());
        disc.setMiscLineup(parser.getOtherLineup());
        return disc;
    }

    private List<PartialReview> parseReviewList(long discId) {
        List<PartialReview> partialReviews = new ArrayList<>();
        Element tableElement = this.doc.select("#review_list > tbody:nth-child(1)").first();
        if (tableElement != null) {
            Elements rows = tableElement.getElementsByTag("tr");
            for (Element row : rows) {
                Elements cells = row.getElementsByTag("td");
                long id = Long.parseLong(cells.get(0).getElementsByTag("a").attr("href").replaceAll("^.*/", ""));
                String name = cells.get(1).text();
                int percentage = Integer.parseInt(cells.get(2).text().replaceAll("%", ""));
                String author = cells.get(3).text();
                String date = cells.get(4).text();
                partialReviews.add(new PartialReview(id, discId, name, percentage, author, date));
            }
        }
        return partialReviews;
//        }
    }

    private PartialImage parseDiscArtwork() {
        String artworkURL = parseImageURL("album_img");
        if (artworkURL != null) {
            return new PartialImage(artworkURL);
        }
        return null;
    }

    private final List<PartialBand> parseSplitBands() {
        List<PartialBand> list = new ArrayList<>();
        Element bandsElement = this.doc.getElementsByClass("band_name").get(0);
        Elements bands = bandsElement.select(("a[href]"));
        String fullString = bandsElement.text();
        for (Element bandElem : bands) {
            String bandName = bandElem.text();
            String hrefAttr = bandElem.attr("href");
            String bandId = hrefAttr.substring(hrefAttr.lastIndexOf("/") + 1);
            list.add(new PartialBand(Long.parseLong(bandId), bandName));
        }
        for (PartialBand partialBand : list) {
            fullString = fullString.replaceFirst(partialBand.getName() + "(?:\\s/)?", "").trim();
        }
        if (!fullString.isEmpty()) {
            for (String leftover : fullString.split("/")) {
                list.add(new PartialBand(0L, leftover.trim()));
            }
        }
        logger.debug("SplitBands: " + list);
        return list;
    }

    private PartialBand parseBand() {
        return new PartialBand(parseBandId(), parseBandName());
    }

    private String parseBandName() {
        Element element = this.doc.getElementsByClass("band_name").get(0);
        String bandName = element.text();
        logger.debug("BandName: " + bandName);
        return bandName;
    }

    private long parseBandId() {
        String bandId = this.doc.select("a[href~=#band_tab_discography]").first().attr("href");
        bandId = bandId.substring(bandId.lastIndexOf("/") + 1, bandId.lastIndexOf("#"));
        return Long.parseLong(bandId);
    }

    @Override
    protected String getSiteURL() {
        return MetallumURL.assembleDiscURL(this.entityId);
    }
}
