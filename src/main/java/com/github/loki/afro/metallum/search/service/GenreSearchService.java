package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.core.parser.search.GenreSearchParser;

public class GenreSearchService extends AbstractBandSearchService {

    public GenreSearchService() {
        super(0, false, false, false, false);
    }

    @Override
    protected GenreSearchParser getSearchParser() {
        return new GenreSearchParser();
    }

}
