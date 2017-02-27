package com.github.loki.afro.metallum.search.query;

import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.SearchRelevance;

import java.util.List;
import java.util.SortedMap;

public class LyricalThemesSearchQuery extends AbstractSearchQuery<Band> {

    public LyricalThemesSearchQuery() {
        super(new Band());
    }

    public LyricalThemesSearchQuery(final Band inputGeneric) {
        super(inputGeneric);
    }

    @Override
    protected final String assembleSearchQuery(final int startPage) {
        final String themes = this.searchObject.getLyricalThemes();
        this.isAValidQuery = !themes.isEmpty();
        return MetallumURL.assembleLyricalThemesSearchURL(themes, startPage);
    }

    public void setLyricalThemes(final String themes) {
        this.searchObject.setLyricalThemes(themes);
    }

    @Override
    protected void setSpecialFieldsInParser(final AbstractSearchParser<Band> parser) {
        // there is nothing to set, because this is a simple query
    }

    @Override
    public void reset() {
        this.searchObject = new Band();
    }

    @Override
    protected SortedMap<SearchRelevance, List<Band>> enrichParsedEntity(final SortedMap<SearchRelevance, List<Band>> resultMap) {
        // nothing to enrich here.
        return resultMap;
    }

}
