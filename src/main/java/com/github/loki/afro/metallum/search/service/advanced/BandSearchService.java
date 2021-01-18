package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.core.parser.search.BandSearchParser;
import com.github.loki.afro.metallum.core.parser.site.BandSiteParser;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.AbstractSearchService;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.google.common.collect.Iterables;

import java.util.Set;
import java.util.function.Function;

public final class BandSearchService extends AbstractSearchService<Band, BandQuery, SearchBandResult> {

    private boolean loadImages;
    private boolean loadSimilar;
    private boolean loadLinks = false;
    private boolean loadReadMore;

    public BandSearchService() {
        this.loadImages = false;
        this.loadSimilar = false;
        this.loadReadMore = false;
    }

    public final void setLoadReadMore(final boolean loadReadMore) {
        this.loadReadMore = loadReadMore;
    }

    public final void setLoadImages(final boolean loadImages) {
        this.loadImages = loadImages;
    }

    public final void setLoadSimilar(final boolean loadSimilar) {
        this.loadSimilar = loadSimilar;
    }

    public final void setLoadLinks(final boolean loadLinks) {
        this.loadLinks = loadLinks;
    }

    @Override
    protected final BandSearchParser getSearchParser(BandQuery bandQuery) {
        BandSearchParser bandSearchParser = new BandSearchParser();
        if (bandQuery.getYearOfFormationFromYear().isPresent() || bandQuery.getYearOfFormationToYear().isPresent()) {
            bandSearchParser.setIsAbleToParseYear(true);
        }
        bandSearchParser.setIsAbleToParseLabel(!bandQuery.getLabelName().orElse("").isEmpty());
        bandSearchParser.setIsAbleToParseLyricalThemes(!bandQuery.getLyricalThemes().orElse("").isEmpty());
        Set<Country> countries = bandQuery.getCountries();
        bandSearchParser.setIsAbleToParseProvince(!bandQuery.getProvince().orElse("").isEmpty() || !countries.isEmpty());
        if (countries.isEmpty()) {
            bandSearchParser.setIsAbleToParseCountry(true);
        } else {
            int foundCountries = 0;
            for (Country country : countries) {
                if (++foundCountries > 1) {
                    bandSearchParser.setIsAbleToParseCountry(true);
                    bandSearchParser.setIsAbleToParseProvince(true);
                }
            }
        }
        return bandSearchParser;
    }

    @Override
    protected Function<Long, Band> getById() {
        return id -> new BandSiteParser(id, this.loadImages, this.loadSimilar, this.loadLinks, this.loadReadMore).parse();
    }

    @Override
    protected void enrichParsedEntity(BandQuery query, SearchBandResult result) {
        Set<Country> countries = query.getCountries();
        if (countries.size() == 1) {
            final Country country = Iterables.getOnlyElement(countries);
            result.setCountry(country);
        }

        Set<BandStatus> statusSet = query.getStatuses();
        if (statusSet.size() == 1) {
            final BandStatus status = Iterables.getOnlyElement(statusSet);
            result.setStatus(status);
        }
    }

}
