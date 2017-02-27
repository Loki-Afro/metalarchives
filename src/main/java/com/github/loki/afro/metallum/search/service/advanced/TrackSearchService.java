package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.core.parser.search.TrackSearchParser;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.AbstractSearchService;

import java.util.List;

/**
 * Represents the advanced search for tracks Here we'll search and try to parse the Result of our
 * Search.
 *
 * How to use this: Setup a TrackSearchQuery and just call performSearch(TrackSearchQuery query)
 *
 * This will also call the Parser to finally get a clear result. <br>
 * <br>
 * <b>What this class does not do:</b> It does not fill the resultList with the whole data what you
 * can get from the metal-archives<br>
 * <br>
 * <b>Actually it does just search!</b>
 *
 * @author Zarathustra
 */
public class TrackSearchService extends AbstractSearchService<Track> {

    private boolean loadLyrics;

    /**
     * Constructs a default TrackSearchService.
     */
    public TrackSearchService() {
        this(false);
    }

    /**
     * Constructs a default TrackSearchService.
     *
     * @param loadLyrics If true, the lyrics will get downlaoded.
     */
    public TrackSearchService(final boolean loadLyrics) {
        this.loadLyrics = loadLyrics;
        // because we can enable the cache this way
        this.objectToLoad = Integer.MAX_VALUE;
    }

    @Override
    protected final TrackSearchParser getSearchParser() {
        return new TrackSearchParser();
    }

    @Override
    public final List<Track> performSearch(final AbstractSearchQuery<Track> query) throws MetallumException {
        ((TrackSearchParser) this.parser).setLoadLyrics(this.loadLyrics);
        return super.performSearch(query);
    }

    public final void setLoadLyrics(final boolean loadLyrics) {
        this.loadLyrics = loadLyrics;
    }

}