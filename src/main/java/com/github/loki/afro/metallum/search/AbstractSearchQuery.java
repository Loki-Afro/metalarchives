package com.github.loki.afro.metallum.search;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.entity.AbstractEntity;

import java.util.List;
import java.util.SortedMap;

public abstract class AbstractSearchQuery<E extends AbstractEntity> {
    protected E searchObject;
    protected boolean isAValidQuery = false;

    public AbstractSearchQuery(final E inputGeneric) {
        this.searchObject = inputGeneric;
    }

    protected String getQuery(final int startPage) throws MetallumException {
        final String searchQuery = assembleSearchQuery(startPage);
        if (this.isAValidQuery) {
            return searchQuery;
        } else {
            throw new MetallumException("No entity to search for!");
        }
    }

    protected abstract String assembleSearchQuery(final int startPage);

    protected abstract void setSpecialFieldsInParser(final AbstractSearchParser<E> parser);

    public void setSearchObject(final E searchObject) {
        this.searchObject = searchObject;
    }

    protected abstract SortedMap<SearchRelevance, List<E>> enrichParsedEntity(SortedMap<SearchRelevance, List<E>> resultMap);

    public abstract void reset();
}
