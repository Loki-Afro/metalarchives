package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.parser.search.BandsByModificationDateSearchParser;
import com.github.loki.afro.metallum.core.parser.site.BandSiteParser;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.search.AbstractSearchService;
import com.github.loki.afro.metallum.search.query.entity.BandByModificationDateResult;
import com.github.loki.afro.metallum.search.query.entity.DateQuery;

import java.util.function.Function;

public class BandsByModificationDateService extends AbstractSearchService<Band, DateQuery, BandByModificationDateResult> {

    @Override
    protected AbstractSearchParser<BandByModificationDateResult> getSearchParser(DateQuery query) {
        return new BandsByModificationDateSearchParser();
    }

    @Override
    protected Function<Long, Band> getById() {
        return id -> new BandSiteParser(id, false, false, false).parse();
    }
}
