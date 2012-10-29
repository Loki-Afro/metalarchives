package de.loki.metallum.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import de.loki.metallum.MetallumException;
import de.loki.metallum.core.parser.search.AbstractSearchParser;
import de.loki.metallum.core.parser.site.AbstractSiteParser;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.AbstractEntity;
import de.loki.metallum.search.cache.Cache;

public abstract class AbstractSearchService<T extends AbstractEntity> {

	protected volatile SortedMap<SearchRelevance, List<T>>	resultMap;
	protected int											objectToLoad	= 0;
	protected AbstractSearchParser<T>						parser			= getSearchParser();

	public AbstractSearchService(final int objectsToLoad) {
		this.resultMap = new TreeMap<SearchRelevance, List<T>>();
		this.objectToLoad = objectsToLoad;
	}

	public AbstractSearchService() {
		this.resultMap = new TreeMap<SearchRelevance, List<T>>();
	}

	/**
	 * Will load so many objectToLoad till the search relevance changes or if
	 * there are no results more with the highest search relevance. If there are
	 * 4 SearchObjects with 5.56577(Search relevance) and objects to load is 2,
	 * You will get 2 complete SearchObjects and 2 uncompleted. You cannot set
	 * the Level to 0 or less.
	 * 
	 * @param objectToLoad the objects to load with the same search relevance
	 */
	protected void setObjectsToLoad(final int objectToLoad) {
		if (objectToLoad >= 0) {
			this.objectToLoad = objectToLoad;
		}

	}

	public List<T> performSearch(final AbstractSearchQuery<T> query) throws MetallumException {
		query.setSpecialFieldsInParser(this.parser);
		if (query.searchObject.getId() == 0) {
			parseSearchResults(query);
		} else {
			// direct id
			final List<T> list = new ArrayList<T>();
			list.add(query.searchObject);
			this.resultMap.put(new SearchRelevance(0d), list);

		}
		loadResults(query);
		return getResultAsList();
	}

	private final void parseSearchResults(final AbstractSearchQuery<T> query) throws MetallumException {
		try {
			int startPage = 0;
			SortedMap<SearchRelevance, List<T>> tempResultMap = new TreeMap<SearchRelevance, List<T>>();
			while (startPage == 0 || this.parser.getTotalSearchResults() > startPage) {
				final String searchUrl = query.getQuery(startPage);
				final String resultHtml = Downloader.getHTML(searchUrl);
				tempResultMap = this.parser.parseSearchResults(resultHtml);
				addToResultMap(tempResultMap);
				startPage += 200;
			}
		} catch (final ExecutionException e) {
			throw new MetallumException(e);
		}
	}

	private void addToResultMap(final SortedMap<SearchRelevance, List<T>> newMap) {
		for (final SearchRelevance searchRelevance : newMap.keySet()) {
			final List<T> listFromResultMap = this.resultMap.get(searchRelevance);
			if (listFromResultMap != null) {
				listFromResultMap.addAll(newMap.get(searchRelevance));
			} else {
				this.resultMap.put(searchRelevance, newMap.get(searchRelevance));
			}
		}
	}

	protected abstract AbstractSearchParser<T> getSearchParser();

	public final T getFirst() {
		List<T> firstList = this.getFirstList();
		if (!firstList.isEmpty()) {
			return firstList.get(0);
		} else {
			return null;
		}
	}

	private final List<T> getFirstList() {
		if (this.resultMap.isEmpty()) {
			return new ArrayList<T>();
		}
		return this.resultMap.get(this.resultMap.firstKey());
	}

	/**
	 * This list contains all elements which were parsed.
	 * 
	 * @return the resultList
	 */
	public final List<T> getResultAsList() {
		final List<T> listToReturn = new ArrayList<T>();
		for (final List<T> listWithSearchObjects : this.resultMap.values()) {
			listToReturn.addAll(listWithSearchObjects);
		}
		return listToReturn;
	}

	public final Map<SearchRelevance, List<T>> getResultMap() {
		return this.resultMap;
	}

	/**
	 * @return true if there is more than one result true, false if there is also no result
	 */
	public final boolean hasMoreAsOneResult() {
		if (this.resultMap.size() > 1) {
			return true;
		}
		if (this.getFirstList().size() > 1) {
			return true;

		}
		return false;

	}

	/**
	 * @return If there is at least element that is in the List this method will return true,
	 *         otherwise false.
	 */
	public final boolean isResultEmpty() {
		return this.resultMap.isEmpty();
	}

	protected final void removeAndPutNewKey(final SearchRelevance key, final List<T> newObjectList) {
		this.resultMap.remove(key);
		this.resultMap.put(key, newObjectList);
	}

	/**
	 * Loads the specific entities into the map. If objectToLoad is 5 you'll
	 * have 5 requests. But only for the SearchRelevance with the highest value.
	 * 
	 * @return {@code getResultAsList}
	 */
	private final void loadResults(final AbstractSearchQuery<T> query) throws MetallumException {
		if (!this.resultMap.isEmpty()) {
			final SearchRelevance key = this.resultMap.firstKey();
			final CopyOnWriteArrayList<T> entityListFromMap = new CopyOnWriteArrayList<T>(this.resultMap.get(key));
			int i = 0;
			for (final T entity : entityListFromMap) {
				if (i < this.objectToLoad) {
					entityListFromMap.set(i, getParsedEntity(entity));
				} else {
					final T entityFromCache = getEntityFromCache(entity);
					entityListFromMap.set(i, (entityFromCache != null ? entityFromCache : entity));
				}
				i++;
			}
			removeAndPutNewKey(key, entityListFromMap);
		}
		this.resultMap = query.enrichParsedEntity(this.resultMap);
	}

	protected final T getParsedEntity(final T entity) throws MetallumException {
		final T newEntitiy;

		final T entityFromCache = getEntityFromCache(entity);
		if (entityFromCache != null && hasAllInformation(entityFromCache)) {
			newEntitiy = entityFromCache;
		} else {
			// final T newEntitiy = useSiteParser((entityFromCache != null ? (T) entityFromCache :
			// entity));
			newEntitiy = useSiteParser(entity);
			Cache.getInstance().put(newEntitiy);
		}
		return newEntitiy;
	}

	@SuppressWarnings("unchecked")
	private T getEntityFromCache(final T entity) {
		final T entityFromCache = (T) Cache.getInstance().getEntity(entity, entity.getClass());
		// has more information? - merge entity with entityFromCache
		return entityFromCache;
	}

	protected abstract boolean hasAllInformation(final T entityFromCache);

	/**
	 * If there is a parser specified you'll get the whole parsed entity.
	 * 
	 * @param entity the parsed entity
	 * @return If there is no parser, the same entity will be returned! otherwise the parsed entity,
	 *         with all informations
	 */
	private final T useSiteParser(final T entity) throws MetallumException {
		AbstractSiteParser<T> parser;
		try {
			parser = getSiteParser(entity);
		} catch (ExecutionException e) {
			throw new MetallumException(e);
		}
		if (parser != null) {
			return parser.parse();
		} else {
			return entity;
		}
	}

	protected AbstractSiteParser<T> getSiteParser(final T entity) throws ExecutionException {
		return null;
	}

}
