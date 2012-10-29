package de.loki.metallum.search.query;

import java.util.List;
import java.util.SortedMap;

import de.loki.metallum.core.parser.search.AbstractSearchParser;
import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.entity.Band;
import de.loki.metallum.search.AbstractSearchQuery;
import de.loki.metallum.search.SearchRelevance;

public class LyricalThemesSearchQuery extends AbstractSearchQuery<Band> {

	public LyricalThemesSearchQuery() {
		super(new Band());
	}

	public LyricalThemesSearchQuery(final Band inputGeneric) {
		super(inputGeneric);
	}

	@Override
	protected final String assembleSearchQuery(final int startPage) {
		final String themes = this.searchObject.getLyricalThemes();
		this.isAValidQuery = !themes.isEmpty();
		return MetallumURL.assembleLyricalThemesSearchURL(themes, startPage);
	}

	public void setLyricalThemes(final String themes) {
		this.searchObject.setLyricalThemes(themes);
	}

	@Override
	protected void setSpecialFieldsInParser(final AbstractSearchParser<Band> parser) {
		// there is nothing to set, because this is a simple query
	}

	@Override
	public void reset() {
		this.searchObject = new Band();
	}

	@Override
	protected SortedMap<SearchRelevance, List<Band>> enrichParsedEntity(final SortedMap<SearchRelevance, List<Band>> resultMap) {
		// nothing to enrich here.
		return resultMap;
	}

}
