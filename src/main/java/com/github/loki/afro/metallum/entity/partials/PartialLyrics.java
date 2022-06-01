package com.github.loki.afro.metallum.entity.partials;

import com.github.loki.afro.metallum.core.parser.site.helper.disc.LyricsSiteParser;

public class PartialLyrics extends IdentifiablePartial<String> {

    public PartialLyrics(long trackId, String name) {
        super(trackId, name);
    }

    @Override
    public String load() {
        LyricsSiteParser lyricsSiteParser = new LyricsSiteParser(this.getId());
        return lyricsSiteParser.parseLyrics();
    }
}
