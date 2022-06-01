package com.github.loki.afro.metallum.search.service.advanced;

import com.github.loki.afro.metallum.core.parser.search.DiscSearchParser;
import com.github.loki.afro.metallum.core.parser.site.DiscSiteParser;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.search.AbstractSearchService;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;

import java.util.function.Function;

public class DiscSearchService extends AbstractSearchService<Disc, DiscQuery, SearchDiscResult> {

    public DiscSearchService() {
    }

    @Override
    protected DiscSearchParser getSearchParser(DiscQuery discQuery) {
        return new DiscSearchParser(discQuery);
    }

    @Override
    protected Function<Long, Disc> getById() {
        return id -> new DiscSiteParser(id).parse();
    }

}
