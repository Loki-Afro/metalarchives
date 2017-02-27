package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.core.parser.site.BandSiteParser;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.search.AbstractSearchService;

import java.util.concurrent.ExecutionException;

public abstract class AbstractBandSearchService extends AbstractSearchService<Band> {

    protected boolean loadImages = false;
    protected boolean loadReviews = false;
    protected boolean loadSimilar = false;
    protected boolean loadLinks = false;
    protected boolean loadReadMore = false;

    public AbstractBandSearchService(final int objectToLoad, final boolean loadImages, final boolean loadReviews, final boolean loadSimilar, final boolean loadReadMore) {
        this.objectToLoad = objectToLoad;
        this.loadImages = loadImages;
        this.loadReviews = loadReviews;
        this.loadSimilar = loadSimilar;
        this.loadReadMore = loadReadMore;
    }

    public final void setLoadReadMore(final boolean loadReadMore) {
        this.loadReadMore = loadReadMore;
    }

    public final void setLoadImages(final boolean loadImages) {
        this.loadImages = loadImages;
    }

    public final void setLoadReviews(final boolean loadReviews) {
        this.loadReviews = loadReviews;
    }

    public final void setLoadSimilar(final boolean loadSimilar) {
        this.loadSimilar = loadSimilar;
    }

    public final void setLoadLinks(final boolean loadLinks) {
        this.loadLinks = loadLinks;
    }

    @Override
    protected final BandSiteParser getSiteParser(final Band band) throws ExecutionException {
        return new BandSiteParser(band, this.loadImages, this.loadReviews, this.loadSimilar, this.loadLinks, this.loadReadMore);
    }

    @Override
    public void setObjectsToLoad(final int objectToLoad) {
        super.setObjectsToLoad(objectToLoad);
    }
}
