package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.core.parser.search.LabelSearchParser;
import com.github.loki.afro.metallum.core.parser.site.LabelSiteParser;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.search.AbstractSearchService;
import com.github.loki.afro.metallum.search.query.entity.LabelQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchLabelResult;

import java.util.function.Function;

public class LabelSearchService extends AbstractSearchService<Label, LabelQuery, SearchLabelResult> {

    private LabelSiteParser.PARSE_STYLE loadCurrentRooster = LabelSiteParser.PARSE_STYLE.NONE;
    private LabelSiteParser.PARSE_STYLE loadPastRooster = LabelSiteParser.PARSE_STYLE.NONE;
    private LabelSiteParser.PARSE_STYLE loadReleases = LabelSiteParser.PARSE_STYLE.NONE;
    private boolean loadLinks = false;

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
    protected Function<Long, Label> getById() {
        return id -> new LabelSiteParser(id, this.loadLinks, this.loadCurrentRooster, this.loadPastRooster, this.loadReleases).parse();
    }

}
