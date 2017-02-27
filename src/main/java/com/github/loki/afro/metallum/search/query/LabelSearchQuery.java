package com.github.loki.afro.metallum.search.query;

import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.entity.Label;
import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.SearchRelevance;

import java.util.List;
import java.util.SortedMap;

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
