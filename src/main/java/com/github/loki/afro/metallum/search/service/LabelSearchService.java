package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.parser.search.LabelSearchParser;
import com.github.loki.afro.metallum.core.parser.site.LabelSiteParser;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.AbstractSearchService;
import com.github.loki.afro.metallum.search.SearchRelevance;
import com.github.loki.afro.metallum.search.query.entity.LabelQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchLabelResult;

import java.util.List;
import java.util.SortedMap;

public class LabelSearchService extends AbstractSearchService<Label, LabelQuery, SearchLabelResult> {

    private boolean loadImages;
    private LabelSiteParser.PARSE_STYLE loadCurrentRooster = LabelSiteParser.PARSE_STYLE.NONE;
    private LabelSiteParser.PARSE_STYLE loadPastRooster = LabelSiteParser.PARSE_STYLE.NONE;
    private LabelSiteParser.PARSE_STYLE loadReleases = LabelSiteParser.PARSE_STYLE.NONE;
    private boolean loadLinks = false;

    public LabelSearchService() {
        this(false);
    }

    public LabelSearchService(final boolean loadImages) {
        this.loadImages = loadImages;
    }



    public void setLoadImages(boolean loadImages) {
        this.loadImages = loadImages;
    }

    public void setLoadCurrentRooster(LabelSiteParser.PARSE_STYLE style) {
        this.loadCurrentRooster = style;
    }

    public void setLoadPstRooster(LabelSiteParser.PARSE_STYLE style) {
        this.loadPastRooster = style;
    }

    public void setLoadReleases(LabelSiteParser.PARSE_STYLE style) {
        this.loadReleases = style;
    }

    public void setLoadLinks(final boolean loadLinks) {
        this.loadLinks = loadLinks;
    }

    @Override
    protected LabelSearchParser getSearchParser(LabelQuery query) {
        return new LabelSearchParser();
    }

    @Override
    protected LabelSiteParser getSiteParser(final long entityId) {
        return new LabelSiteParser(entityId, this.loadImages, this.loadLinks, this.loadCurrentRooster, this.loadPastRooster, this.loadReleases);
    }

}
