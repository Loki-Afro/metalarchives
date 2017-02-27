package com.github.loki.afro.metallum.core.parser.site.helper.disc;

import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.Track;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class DiscSiteTrackParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscSiteTrackParser.class);
    private CountDownLatch doneSignal;
    private final Document doc;
    private final boolean isSplit;
    private final boolean loadLyrics;

    public DiscSiteTrackParser(final Document doc, final boolean isSplit, final boolean loadLyrics) {
        this.doc = doc;
        this.isSplit = isSplit;
        this.loadLyrics = loadLyrics;
    }

    private boolean parseIsInstrumental(final Element row) {
        Element lastTd = row.getElementsByTag("td").last();
        return lastTd.text().contains("instrumental");
    }

    /**
     * if doneSignal is not initialized this method will initialize it with the given count of threads,
     *
     * @param trackCount count of tracks to parse
     */
    private void lazyInitLatch(final int trackCount) {
        if (this.doneSignal == null) {
            this.doneSignal = new CountDownLatch(trackCount);
        }
    }

    private void parseLyrics2(final Element row, final long trackId, final Track trackToModify) {
        new Thread(new DownloadLyricsRunnable(trackToModify, trackId)).start();
    }

    private final class DownloadLyricsRunnable implements Runnable {

        private final Track trackToModify;
        private final long trackId;

        DownloadLyricsRunnable(final Track trackToModify, final long trackId) {
            this.trackToModify = trackToModify;
            this.trackId = trackId;
        }

        @Override
        public void run() {
            try {
                String lyricsHTML = Downloader.getHTML(MetallumURL.assembleLyricsURL(this.trackId));
                lyricsHTML = lyricsHTML.replaceAll("<br />", System.getProperty("line.separator")).replaceAll("(lyrics not available)", "");
                this.trackToModify.setLyrics(lyricsHTML.trim());
            } catch (final ExecutionException e) {
                LOGGER.error("unable to get lyrics from \"" + this.trackId + "\"", e);
            } finally {
                signalDoneCountDown();
            }
        }
    }

    private void signalDoneCountDown() {
        if (this.doneSignal != null) {
            DiscSiteTrackParser.this.doneSignal.countDown();
        }
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

    public Track[] parse() {
        Element tableElement = this.doc.select("table[class$=table_lyrics]").first();
        int counter = 1;
        boolean foundFirstFirstTrack = false;
        Elements rows = tableElement.select("tr[class~=(even|odd)]");
        List<Track> trackList = new ArrayList<>();
        for (final Element row : rows) {
            final String trackIdStr = parseTrackId(row);
            final long trackId = Long.parseLong(trackIdStr.replaceAll("[\\D]", ""));
            Track track = new Track(trackId);
            track.setName(parseTrackTitle(row, this.isSplit));
            track.setPlayTime(parsePlayTime(row));
            int trackNumber = parseTrackNumber(row);
            track.setTrackNumber(trackNumber);
            if (foundFirstFirstTrack && trackNumber == 1) {
                counter++;
            } else if (trackNumber == 1) {
                foundFirstFirstTrack = true;
            }
            track.setDiscNumber(counter);
//			because otherwise the bandName is always the same
            if (this.isSplit) {
                track.setBandName(parseBandName(row));
            }
            track.setInstrumental(parseIsInstrumental(row));
            if (this.loadLyrics && row.getElementById("lyricsButton" + trackIdStr) != null) {
                lazyInitLatch(rows.size());
                parseLyrics2(row, trackId, track);
            } else {
                signalDoneCountDown();
            }
            trackList.add(track);

        }
        waitUntilEverythingIsDone(rows.size());
        return trackList.toArray(new Track[trackList.size()]);
    }

    private void waitUntilEverythingIsDone(final int trackCount) {
        if (this.loadLyrics && this.doneSignal != null) {
            try {
//				as fallback we wait here 6 seconds for each track if that fails smth went wrong
                this.doneSignal.await((long) trackCount * 6, TimeUnit.SECONDS);
            } catch (final InterruptedException e) {
                LOGGER.error("Please, please report this error: Thread Lock failed while downloading lyrics", e);
                Thread.currentThread().interrupt();
            }
        }
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
            title = title.substring(title.indexOf(" - ") + 3, title.length());
            return title;
        } else {
            return title;
        }
    }

    private String parsePlayTime(final Element row) {
        return row.getElementsByTag("td").get(2).text();
    }

}
