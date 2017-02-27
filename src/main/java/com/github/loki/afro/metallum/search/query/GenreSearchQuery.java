package com.github.loki.afro.metallum.search.query;

import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.SearchRelevance;

import java.util.List;
import java.util.SortedMap;

public class GenreSearchQuery extends AbstractSearchQuery<Band> {

    public GenreSearchQuery(final Band inputGeneric) {
        super(inputGeneric);
    }

    public GenreSearchQuery() {
        super(new Band(0));
    }

    @Override
    protected final String assembleSearchQuery(final int startPage) {
        final String genreToSearchFor = this.searchObject.getGenre();
        if (!genreToSearchFor.isEmpty()) {
            this.isAValidQuery = true;
        }
        return MetallumURL.assembleGenreSearchURL(genreToSearchFor, startPage);
    }

    public void setGenre(final String genreName) {
        this.searchObject.setGenre(genreName);
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
        return resultMap;
    }

}
