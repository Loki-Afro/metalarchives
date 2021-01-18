package com.github.loki.afro.metallum.search;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.AbstractEntity;
import com.github.loki.afro.metallum.entity.Identifiable;
import com.github.loki.afro.metallum.search.query.entity.IQuery;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import java.util.*;
import java.util.function.Function;

public abstract class AbstractSearchService<FULL_ENTITY extends AbstractEntity, QUERY extends IQuery, SEARCH_RESULT extends Identifiable> {

    public AbstractSearchService() {
    }

    protected void enrichParsedEntity(QUERY query, SEARCH_RESULT resultMap) {
    }

    protected abstract AbstractSearchParser<SEARCH_RESULT> getSearchParser(QUERY query);


    public List<SEARCH_RESULT> get(QUERY query) {
        return new ArrayList<>(toList(getAsMap(query)));
    }

    private SortedMap<SearchRelevance, Collection<SEARCH_RESULT>> getAsMap(QUERY query) {
        ListMultimap<SearchRelevance, SEARCH_RESULT> resultMap = parseSearchResults(query);

        Map<SearchRelevance, Collection<SEARCH_RESULT>> searchRelevanceCollectionMap = resultMap.asMap();
        return new TreeMap<>(searchRelevanceCollectionMap);
    }

    public List<FULL_ENTITY> getFully(QUERY query) {
        List<FULL_ENTITY> out = new ArrayList<>();
        SortedMap<SearchRelevance, Collection<SEARCH_RESULT>> resultAsMap = getAsMap(query);

        for (Map.Entry<SearchRelevance, Collection<SEARCH_RESULT>> searchResultMapEntry : resultAsMap.entrySet()) {
            for (SEARCH_RESULT searchResult : searchResultMapEntry.getValue()) {
                FULL_ENTITY fullEntity = parseFully().apply(searchResult);
                out.add(fullEntity);
            }
        }

        return out;
    }


    @Deprecated
    public List<SEARCH_RESULT> performSearch(final AbstractSearchQuery<QUERY> query) throws MetallumException {
        return new ArrayList<>(get(query.searchObject));
    }

    @Deprecated
    public List<FULL_ENTITY> performSearchAndLoadFully(final AbstractSearchQuery<QUERY> query) throws MetallumException {
        return getFully(query.searchObject);
    }

    public FULL_ENTITY getById(long id) {
        return getById().apply(id);
    }

    private final ListMultimap<SearchRelevance, SEARCH_RESULT> parseSearchResults(QUERY query) throws MetallumException {
        ListMultimap<SearchRelevance, SEARCH_RESULT> map = MultimapBuilder.treeKeys()
                .arrayListValues()
                .build();

        final AbstractSearchParser<SEARCH_RESULT> parser = getSearchParser(query);
        int page = 0;
        while (page == 0 || parser.getTotalSearchResults() > page) {
            final String searchUrl = getUrlForQuery(query, page);
            final String resultHtml = Downloader.getHTML(searchUrl);
            SortedMap<SearchRelevance, List<SEARCH_RESULT>> newMap = parser.parseSearchResults(resultHtml);

            for (Map.Entry<SearchRelevance, List<SEARCH_RESULT>> entry : newMap.entrySet()) {
                for (SEARCH_RESULT searchResult : entry.getValue()) {
                    enrichParsedEntity(query, searchResult);
                    map.put(entry.getKey(), searchResult);
                }
            }

            page += 200;
        }
        return map;
    }

    private String getUrlForQuery(QUERY query, final int startPage) throws MetallumException {
        if (query.isValid()) {
            return query.assembleQueryUrl(startPage);
        } else {
            throw new MetallumException("No entity to search for!");
        }
    }

    private static <X> List<X> toList(Map<SearchRelevance, Collection<X>> map) {
        final List<X> listToReturn = new ArrayList<>();
        for (final Collection<X> listWithSearchObjects : map.values()) {
            listToReturn.addAll(listWithSearchObjects);
        }
        return listToReturn;
    }

    protected Function<SEARCH_RESULT, FULL_ENTITY> parseFully() {
        return sr -> getById(sr.getId());
    }

    protected abstract Function<Long, FULL_ENTITY> getById();

    public FULL_ENTITY getSingleUniqueByQuery(QUERY query) {
        return Iterables.getOnlyElement(getFully(query));
    }


}
