package com.github.loki.afro.metallum.core.parser.search;

import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import org.json.JSONArray;
import org.json.JSONException;

public class BandSearchParser extends AbstractSearchParser<SearchBandResult> {

    private boolean isAbleToParseProvince = false;
    private boolean isAbleToParseLabel = false;
    private boolean isAbleToParseYear = false;
    private boolean isAbleToParseLyricalThemes = false;
    private boolean isAbleToParseCountry = false;

    public void setIsAbleToParseCountry(final boolean isAbleToParse) {
        this.isAbleToParseCountry = isAbleToParse;
    }

    public void setIsAbleToParseProvince(final boolean isAbleToParse) {
        this.isAbleToParseProvince = isAbleToParse;
    }

    public void setIsAbleToParseYear(final boolean isAbleToParse) {
        this.isAbleToParseYear = isAbleToParse;
    }

    public void setIsAbleToParseLyricalThemes(final boolean isAbleToParse) {
        this.isAbleToParseLyricalThemes = isAbleToParse;
    }

    public void setIsAbleToParseLabel(final boolean isAbleToParse) {
        this.isAbleToParseLabel = isAbleToParse;
    }

    @Override
    protected final SearchBandResult useSpecificSearchParser(final JSONArray hits) throws JSONException {
        SearchBandResult searchBandResult = new SearchBandResult(parseId(hits.getString(0)), parseName(hits.getString(0)));
        searchBandResult.setGenre(parseGenres(hits.getString(1)));

        int index = 2;
        if (isAbleToParseCountry) {
            searchBandResult.setCountry(parseCountry(hits.getString(index++)));
        }
        if (isAbleToParseProvince) {
            searchBandResult.setProvince(parseProvince(hits.getString(index++)));
        }
        if (isAbleToParseLyricalThemes) {
            searchBandResult.setLyricalThemes(parseLyricalThemes(hits.getString(index++)));
        }
        if (isAbleToParseYear) {
            searchBandResult.setYearFormedIn(parseYear((hits.getString(index++))));
        }
        if (isAbleToParseLabel) {
            searchBandResult.setLabelName(parseLabel(hits.getString(index)));
        }

        return searchBandResult;
    }

    private String parseProvince(final String hit) {
        // currently we do not really have to parse
        return hit.trim();
    }

    private String parseLyricalThemes(final String hit) {
        // currently we do not really have to parse
        return hit;
    }

    private int parseYear(final String hit) {
        // currently we do not really have to parse
        return Integer.parseInt(hit);
    }

    private String parseLabel(final String hit) {
        // currently we do not really have to parse
        return hit;
    }

    private String parseGenres(final String hitPart) {
        // currently we do not really have to parse
        return hitPart;
    }

    private Country parseCountry(final String hitPart) {
        // currently we do not really have to parse
        return Country.ofMetallumDisplayName(hitPart);
    }

    private String parseName(String hitPart) {
        hitPart = hitPart.substring(hitPart.indexOf(">") + 1);
        return hitPart.substring(0, hitPart.indexOf("</a>"));
    }

    private long parseId(String hitPart) {
        hitPart = hitPart.substring(hitPart.indexOf("/bands/") + 7, hitPart.indexOf(">") - 1);
        return Long.parseLong((hitPart.replaceAll(".*?/", "")));
    }

}
