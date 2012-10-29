package de.loki.metallum.search.service;

import de.loki.metallum.core.parser.search.GenreSearchParser;

public class GenreSearchService extends AbstractBandSearchService {

	public GenreSearchService() {
		super(0, false, false, false, false);
	}

	@Override
	protected GenreSearchParser getSearchParser() {
		return new GenreSearchParser();
	}

}
