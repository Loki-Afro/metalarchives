package com.github.loki.afro.metallum.search;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;
import com.github.loki.afro.metallum.entity.AbstractEntity;
import com.github.loki.afro.metallum.entity.Identifiable;
import com.github.loki.afro.metallum.search.query.entity.IQuery;
import com.google.common.collect.Iterables;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public abstract class AbstractSearchService<FULL_ENTITY extends AbstractEntity, QUERY extends IQuery, SEARCH_RESULT extends Identifiable> {

    protected SortedMap<SearchRelevance, List<SEARCH_RESULT>> searchResultMap = new TreeMap<>();
    protected final SortedMap<SearchRelevance, List<FULL_ENTITY>> fullResultMap = new TreeMap<>();

    public AbstractSearchService() {
    }

    protected SortedMap<SearchRelevance, List<SEARCH_RESULT>> enrichParsedEntity(QUERY query, SortedMap<SearchRelevance, List<SEARCH_RESULT>> resultMap) {
        return resultMap;
    }

    protected abstract AbstractSearchParser<SEARCH_RESULT> getSearchParser(QUERY query);


    public List<SEARCH_RESULT> get(QUERY query) {
        AbstractSearchParser<SEARCH_RESULT> parser = getSearchParser(query);
        parseSearchResults(parser, query);

        loadResults(query);
        return toList(this.searchResultMap);
    }

    public List<FULL_ENTITY> getFully(QUERY query) {
        get(query);

        for (Map.Entry<SearchRelevance, List<SEARCH_RESULT>> searchResultMapEntry : this.searchResultMap.entrySet()) {
            List<FULL_ENTITY> tempList = new ArrayList<>();
            for (SEARCH_RESULT searchResult : searchResultMapEntry.getValue()) {
                FULL_ENTITY fullEntity = parseFully().apply(searchResult);
                tempList.add(fullEntity);
            }

            if (!tempList.isEmpty()) {
                this.fullResultMap.put(searchResultMapEntry.getKey(), tempList);
            }
        }

        return toList(this.fullResultMap);
    }


    @Deprecated
    public List<SEARCH_RESULT> performSearch(final AbstractSearchQuery<QUERY> query) throws MetallumException {
        return get(query.searchObject);
    }

    @Deprecated
    public List<FULL_ENTITY> performSearchAndLoadFully(final AbstractSearchQuery<QUERY> query) throws MetallumException {
        return getFully(query.searchObject);
    }

    public FULL_ENTITY getById(long id) {
        FULL_ENTITY fullEntity = getById().apply(id);
        this.fullResultMap.put(new SearchRelevance(0d), Collections.singletonList(fullEntity));
        return fullEntity;
    }

    private final void parseSearchResults(AbstractSearchParser<SEARCH_RESULT> parser, QUERY query) throws MetallumException {
        int startPage = 0;
        while (startPage == 0 || parser.getTotalSearchResults() > startPage) {
            final String searchUrl = getUrlForQuery(query, startPage);
            final String resultHtml = Downloader.getHTML(searchUrl);
            addToSearchResultMap(parser.parseSearchResults(resultHtml));
            startPage += 200;
        }
    }

    private String getUrlForQuery(QUERY query, final int startPage) throws MetallumException {
        if (query.isValid()) {
            return query.assembleQueryUrl(startPage);
        } else {
            throw new MetallumException("No entity to search for!");
        }
    }

    private void addToSearchResultMap(final SortedMap<SearchRelevance, List<SEARCH_RESULT>> newMap) {
        for (final SearchRelevance searchRelevance : newMap.keySet()) {
            final List<SEARCH_RESULT> listFromSEARCHResultMap = this.searchResultMap.get(searchRelevance);
            if (listFromSEARCHResultMap != null) {
                listFromSEARCHResultMap.addAll(newMap.get(searchRelevance));
            } else {
                this.searchResultMap.put(searchRelevance, newMap.get(searchRelevance));
            }
        }
    }

    private static <X> List<X> toList(Map<SearchRelevance, List<X>> map) {
        final List<X> listToReturn = new ArrayList<>();
        for (final List<X> listWithSearchObjects : map.values()) {
            listToReturn.addAll(listWithSearchObjects);
        }
        return listToReturn;
    }

    protected final void removeAndPutNewKey(final SearchRelevance key, final List<SEARCH_RESULT> newObjectList) {
        this.searchResultMap.remove(key);
        this.searchResultMap.put(key, newObjectList);
    }

    /**
     * Loads the specific entities into the map. If objectToLoad is 5 you'll
     * have 5 requests. But only for the SearchRelevance with the highest value.
     */
    private final void loadResults(final QUERY query) throws MetallumException {
        if (!this.searchResultMap.isEmpty()) {
            final SearchRelevance key = this.searchResultMap.firstKey();
            final CopyOnWriteArrayList<SEARCH_RESULT> entityListFromMap = new CopyOnWriteArrayList<>(this.searchResultMap.get(key));
            int i = 0;
            for (final SEARCH_RESULT entity : entityListFromMap) {
                entityListFromMap.set(i, entity);
                i++;
            }
            removeAndPutNewKey(key, entityListFromMap);
        }
        this.searchResultMap = enrichParsedEntity(query, this.searchResultMap);
    }

    protected Function<SEARCH_RESULT, FULL_ENTITY> parseFully() {
        return sr -> getById(sr.getId());
    }

    protected abstract Function<Long, FULL_ENTITY> getById();

    public FULL_ENTITY getSingleUniqueByQuery(QUERY query) {
        return Iterables.getOnlyElement(getFully(query));
    }


}
