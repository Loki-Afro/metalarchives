package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.core.parser.search.BandSearchParser;
import com.github.loki.afro.metallum.core.parser.site.BandSiteParser;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.search.AbstractSearchService;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;

import java.util.function.Function;

public final class BandSearchService extends AbstractSearchService<Band, BandQuery, SearchBandResult> {

    private boolean loadImages;
    private boolean loadSimilar;
    private boolean loadLinks = false;
    private boolean loadReadMore;

    public BandSearchService() {
        this.loadImages = false;
        this.loadSimilar = false;
        this.loadReadMore = false;
    }

    public final void setLoadReadMore(final boolean loadReadMore) {
        this.loadReadMore = loadReadMore;
    }

    public final void setLoadImages(final boolean loadImages) {
        this.loadImages = loadImages;
    }

    public final void setLoadSimilar(final boolean loadSimilar) {
        this.loadSimilar = loadSimilar;
    }

    public final void setLoadLinks(final boolean loadLinks) {
        this.loadLinks = loadLinks;
    }

    @Override
    protected final BandSearchParser getSearchParser(BandQuery bandQuery) {
        return new BandSearchParser(bandQuery);
    }

    @Override
    protected Function<Long, Band> getById() {
        return id -> new BandSiteParser(id, this.loadImages, this.loadSimilar, this.loadLinks, this.loadReadMore).parse();
    }


}
