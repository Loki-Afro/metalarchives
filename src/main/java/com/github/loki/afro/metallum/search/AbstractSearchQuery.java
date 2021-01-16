package com.github.loki.afro.metallum.search;

import com.github.loki.afro.metallum.search.query.entity.IQuery;

@Deprecated
public abstract class AbstractSearchQuery<QUERY extends IQuery> {
    protected QUERY searchObject;

    public AbstractSearchQuery(final QUERY inputGeneric) {
        this.searchObject = inputGeneric;
    }

}
