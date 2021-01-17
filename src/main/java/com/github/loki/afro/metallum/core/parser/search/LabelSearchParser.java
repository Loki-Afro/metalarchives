package com.github.loki.afro.metallum.core.parser.search;

import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.SearchRelevance;
import com.github.loki.afro.metallum.search.query.entity.SearchLabelResult;
import com.google.api.client.util.Strings;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;

public class LabelSearchParser extends AbstractSearchParser<SearchLabelResult> {

    @Override
    protected SearchLabelResult useSpecificSearchParser(JSONArray hits) throws JSONException {
        SearchLabelResult label = new SearchLabelResult(parseLabelId(hits.getString(0)), parseLabelName(hits.getString(0)));
        label.setCountry(parseLabelCountry(hits.getString(1)));
        label.setSpecialisation(parseSpecialisation(hits.getString(2)));
        return label;
    }

    private long parseLabelId(final String hit) {
        String id = hit.substring(0, hit.indexOf("\">"));
        id = id.substring(id.lastIndexOf("/") + 1);
        return Long.parseLong(id);
    }

    private String parseLabelName(final String hit) {
        return Jsoup.parse(hit).text();
    }

    private Country parseLabelCountry(final String hit) {
        if (Strings.isNullOrEmpty(hit)) {
            return null;
        } else {
            return Country.ofMetallumDisplayName(hit);
        }
    }

    private String parseSpecialisation(final String hit) {
        return hit;
    }

    @Override
    protected final SearchRelevance getSearchRelevance(JSONArray hits) throws JSONException {
        // actually there is no searchrelevance :'(
        return new SearchRelevance(0d);
    }

}
