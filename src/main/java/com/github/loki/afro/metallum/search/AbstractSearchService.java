package com.github.loki.afro.metallum.search;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.AbstractEntity;
import com.github.loki.afro.metallum.entity.Identifiable;
import com.github.loki.afro.metallum.search.query.entity.IQuery;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractSearchService<FULL_ENTITY extends AbstractEntity, QUERY extends IQuery, SEARCH_RESULT extends Identifiable> {

    public AbstractSearchService() {
    }

    protected abstract AbstractSearchParser<SEARCH_RESULT> getSearchParser(QUERY query);


    public Iterable<SEARCH_RESULT> get(QUERY query) {
        return lazyQuery(query);
    }


    public Iterable<FULL_ENTITY> getFully(QUERY query) {
        return StreamSupport.stream(lazyQuery(query).spliterator(), false)
                .map(r -> parseFully().apply(r))
                ::iterator;
    }


    @Deprecated
    public List<SEARCH_RESULT> performSearch(final AbstractSearchQuery<QUERY> query) throws MetallumException {
        return Lists.newArrayList(get(query.searchObject));
    }

    @Deprecated
    public List<FULL_ENTITY> performSearchAndLoadFully(final AbstractSearchQuery<QUERY> query) throws MetallumException {
        return Lists.newArrayList(getFully(query.searchObject));
    }

    public FULL_ENTITY getById(long id) {
        return getById().apply(id);
    }

    private Iterable<SEARCH_RESULT> lazyQuery(QUERY query) {
        AbstractSearchParser<SEARCH_RESULT> parser = getSearchParser(query);
        Iterator<List<SEARCH_RESULT>> pagingIterator = new AbstractIterator<List<SEARCH_RESULT>>() {
            private boolean endOfData;
            private int offset = 0;
            private static final int PAGE_SIZE = 200;

            @Override
            protected List<SEARCH_RESULT> computeNext() {
                if (endOfData) {
                    return endOfData();
                }

                List<SEARCH_RESULT> rows = query(query, parser, offset);

                if (rows.isEmpty()) {
                    return endOfData();
                } else if (rows.size() < PAGE_SIZE) {
                    endOfData = true;
                } else {
                    offset += PAGE_SIZE;
                }

                return rows;
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(pagingIterator, 0), false)
                .flatMap(List::stream)
                ::iterator;
    }


    private List<SEARCH_RESULT> query(QUERY query, AbstractSearchParser<SEARCH_RESULT> parser, int offset) {
        final String searchUrl = getUrlForQuery(query, offset);
        final String resultHtml = Downloader.getHTML(searchUrl);
        SortedMap<SearchRelevance, List<SEARCH_RESULT>> newMap = parser.parseSearchResults(resultHtml);
        return newMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private String getUrlForQuery(QUERY query, final int offset) throws MetallumException {
        if (query.isValid()) {
            return query.assembleQueryUrl(offset);
        } else {
            throw new MetallumException("invalid query!");
        }
    }

    protected Function<SEARCH_RESULT, FULL_ENTITY> parseFully() {
        return sr -> getById(sr.getId());
    }

    protected abstract Function<Long, FULL_ENTITY> getById();

    public FULL_ENTITY getSingleUniqueByQuery(QUERY query) {
        return Iterables.getOnlyElement(getFully(query));
    }


}
