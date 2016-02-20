package de.loki.metallum.search.query;

import java.util.List;
import java.util.SortedMap;

import de.loki.metallum.core.parser.search.AbstractSearchParser;
import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.entity.Label;
import de.loki.metallum.search.AbstractSearchQuery;
import de.loki.metallum.search.SearchRelevance;

public class LabelSearchQuery extends AbstractSearchQuery<Label> {

	public LabelSearchQuery() {
		// the zero doesen't matter here...
		super(new Label());
	}

	public LabelSearchQuery(final Label inputLabel) {
		super(inputLabel);
	}

	public void setLabelName(final String name) {
		this.searchObject.setName(name);
	}

	@Override
	protected final String assembleSearchQuery(final int startPage) {
		final String labelName = this.searchObject.getName();
		this.isAValidQuery = !labelName.isEmpty();
		return MetallumURL.assembleLabelSearchURL(labelName, startPage);
	}

	@Override
	protected void setSpecialFieldsInParser(final AbstractSearchParser<Label> parser) {
		// there is nothing to set, because this is a simple query
	}

	@Override
	public void reset() {
		this.searchObject = new Label();
	}

	@Override
	protected SortedMap<SearchRelevance, List<Label>> enrichParsedEntity(final SortedMap<SearchRelevance, List<Label>> resultMap) {
		return resultMap;
	}

}
