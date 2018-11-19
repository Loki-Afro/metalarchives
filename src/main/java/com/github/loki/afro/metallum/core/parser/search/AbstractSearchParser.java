package com.github.loki.afro.metallum.core.parser.search;

import com.github.loki.afro.metallum.core.parser.IJSONParser;
import com.github.loki.afro.metallum.entity.AbstractEntity;
import com.github.loki.afro.metallum.search.SearchRelevance;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Parses the data which was gained by the search
 *
 * @author Zarathustra
 */
public abstract class AbstractSearchParser<T extends AbstractEntity> implements IJSONParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSearchParser.class);

    private long totalSearchResults = 0;

    public final long getTotalSearchResults() {
        return this.totalSearchResults;
    }

    public final SortedMap<SearchRelevance, List<T>> parseSearchResults(final String jsonStr) {
        final SortedMap<SearchRelevance, List<T>> resultMap = new TreeMap<>();
        try {
            final JSONObject json = new JSONObject(jsonStr);
            // the total results from the html.
            this.totalSearchResults = json.getInt(TOTAL_SEARCH_RESULTS_FROM_JSON);
            if (this.totalSearchResults > 0) {
                // only possible if totalSearchResults > 0, otherwise exception
                final JSONArray hits = json.getJSONArray(MAIN_JSON_ARRAY_STRING);
                for (int i = 0; i < hits.length(); i++) {
                    final T parsedObject = useSpecificSearchParser(hits.getJSONArray(i));
                    final SearchRelevance relevance = getSearchRelevance(hits.getJSONArray(i));
                    if (resultMap.containsKey(relevance)) {
                        resultMap.get(relevance).add(parsedObject);
                    } else {
                        final List<T> newList = new ArrayList<>();
                        newList.add(parsedObject);
                        resultMap.put(relevance, newList);
                    }
                }
            }
        } catch (final JSONException e) {
            LOGGER.error("maybe not valid html: " + jsonStr, e);
        }
        return resultMap;
    }

    /**
     * The search relevance is how straight the result matches the SearchQuery<br>
     * If there is a search relevance, you will get it, otherwise 0<br>
     * <br>
     * <b>If you don't override this method the String from hits at position 0 will be used.</b>
     *
     * @param hits the JSON hits
     * @return 0 or the search relevance.
     */
    protected SearchRelevance getSearchRelevance(final JSONArray hits) {
        String relevance = hits.getString(0);
        relevance = relevance.substring(relevance.indexOf("<!-- ") + 5, relevance.indexOf(" -->"));
        return new SearchRelevance(relevance);
    }

    /**
     * Parse the id from the JSon hit.
     *
     * @param hit the JSon hit.
     * @return the parsed id.
     */
    long parseBandId(final String hit) {
        String bandId = hit.substring(0, hit.indexOf("\" title"));
        bandId = bandId.substring(bandId.lastIndexOf("/") + 1, bandId.length());
        return Long.parseLong(bandId);
    }

    protected abstract T useSpecificSearchParser(JSONArray hits) throws JSONException;

}
