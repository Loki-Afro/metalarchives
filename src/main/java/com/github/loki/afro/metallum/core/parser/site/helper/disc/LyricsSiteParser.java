package com.github.loki.afro.metallum.core.parser.site.helper.disc;

import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;

public class LyricsSiteParser {

    private final long trackId;

    public LyricsSiteParser(long trackId) {
        this.trackId = trackId;
    }

    public String parseLyrics() {
        String lyricsHTML = Downloader.getHTML(MetallumURL.assembleLyricsURL(this.trackId));
        lyricsHTML = lyricsHTML.replaceAll("<br />", System.getProperty("line.separator"))
                .replaceAll("(lyrics not available)", "");
        return lyricsHTML.trim();
    }
}
