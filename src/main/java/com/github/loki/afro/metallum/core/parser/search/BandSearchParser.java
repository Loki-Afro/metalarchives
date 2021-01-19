package com.github.loki.afro.metallum.core.parser.search;

import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.google.common.collect.Iterables;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Set;

public class BandSearchParser extends AbstractSearchParser<SearchBandResult> {

    private final BandQuery bandQuery;
    private final boolean isAbleToParseProvince;
    private final boolean isAbleToParseLabel;
    private final boolean isAbleToParseLyricalThemes;
    private boolean isAbleToParseYear = false;
    private boolean isAbleToParseCountry = false;

    public BandSearchParser(BandQuery bandQuery) {
        this.bandQuery = bandQuery;
        if (bandQuery.getYearOfFormationFromYear().isPresent() || bandQuery.getYearOfFormationToYear().isPresent()) {
            this.isAbleToParseYear = true;
        }
        this.isAbleToParseLabel = !bandQuery.getLabelName().orElse("").isEmpty();
        this.isAbleToParseLyricalThemes = !bandQuery.getLyricalThemes().orElse("").isEmpty();
        Set<Country> countries = bandQuery.getCountries();
        this.isAbleToParseProvince = !bandQuery.getProvince().orElse("").isEmpty() || !countries.isEmpty();
        if (countries.isEmpty()) {
            this.isAbleToParseCountry = true;
        } else {
            int foundCountries = 0;
            for (Country country : countries) {
                if (++foundCountries > 1) {
                    this.isAbleToParseCountry = true;
                }
            }
        }
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
        Set<Country> countries = this.bandQuery.getCountries();
        if (countries.size() == 1) {
            final Country country = Iterables.getOnlyElement(countries);
            searchBandResult.setCountry(country);
        }

        Set<BandStatus> statusSet = this.bandQuery.getStatuses();
        if (statusSet.size() == 1) {
            final BandStatus status = Iterables.getOnlyElement(statusSet);
            searchBandResult.setStatus(status);
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
