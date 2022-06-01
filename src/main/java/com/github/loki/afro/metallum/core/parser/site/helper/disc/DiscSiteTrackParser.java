package com.github.loki.afro.metallum.core.parser.site.helper.disc;

import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.entity.partials.PartialLyrics;
import com.github.loki.afro.metallum.enums.DiscType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public final class DiscSiteTrackParser {
    private final Document doc;
    private final DiscType discType;
    private final Disc disc;

    public DiscSiteTrackParser(final Document doc, final Disc disc) {
        this.doc = doc;
        this.discType = disc.getType();
        this.disc = disc;
    }

    private boolean parseIsInstrumental(final Element row) {
        Element lastTd = row.getElementsByTag("td").last();
        return lastTd.text().contains("instrumental");
    }

    /*
     * only called when isSplit is true
     */
    private String parseBandName(final Element row) {
        String bandName = row.getElementsByTag("td").get(1).text();
        bandName = bandName.substring(0, bandName.indexOf(" - "));
        return bandName;
    }

    private int parseTrackNumber(final Element row) {
        String trackNo = row.getElementsByTag("td").get(0).text();
        return Integer.parseInt(trackNo.replaceAll("\\.", ""));
    }

    public List<Track> parse() {
        Element tableElement = this.doc.select("table[class$=table_lyrics]").first();
        int counter = 1;
        boolean foundFirstFirstTrack = false;
        Elements rows = tableElement.select("tr[class~=(even|odd)]");
        List<Track> trackList = new ArrayList<>();
        for (final Element row : rows) {
            final String trackIdStr = parseTrackId(row);
            final long trackId = Long.parseLong(trackIdStr.replaceAll("[\\D]", ""));
            String trackTitle = parseTrackTitle(row, this.discType.isSplit());

            // because otherwise the bandName is always the same
            final Track track;
            if (discType == DiscType.COLLABORATION) {
                track = Track.createCollaborationTrack(this.disc, trackId, trackTitle);
            } else if (this.discType.isSplit()) {
                final String bandName = parseBandName(row);
                track = Track.createSplitTrack(this.disc, bandName, trackId, trackTitle);
            } else {
                track = new Track(this.disc, this.disc.getBandName(), trackId, trackTitle);
            }


            track.setPlayTime(parsePlayTime(row));
            int trackNumber = parseTrackNumber(row);
            track.setTrackNumber(trackNumber);
            if (foundFirstFirstTrack && trackNumber == 1) {
                counter++;
            } else if (trackNumber == 1) {
                foundFirstFirstTrack = true;
            }
            track.setDiscNumber(counter);

            track.setInstrumental(parseIsInstrumental(row));
            if (row.getElementById("lyricsButton" + trackIdStr) != null) {
                track.setLyrics(new PartialLyrics(trackId, track.getName()));
            }
            trackList.add(track);

        }
        return trackList;
    }

    private String parseTrackId(final Element row) {
//		<a name="5767" class="anchor"> </a>
        // if it is a tape with 2 sides, like side A, A is attached to the id of the track
        // searching for lyrics however is still possible without the attached A
        return row.select("a[name~=\\d.*]").first().attr("name");
    }

    private String parseTrackTitle(final Element row, final boolean isSplit) {
        String title = row.getElementsByTag("td").get(1).text();
        if (isSplit) {
//			because the result is smth. like: BandName - TitleName
            title = title.substring(title.indexOf(" - ") + 3);
            return title;
        } else {
            return title;
        }
    }

    private String parsePlayTime(final Element row) {
        return row.getElementsByTag("td").get(2).text();
    }

}
