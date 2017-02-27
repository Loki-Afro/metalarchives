package com.github.loki.afro.metallum.search.query;

import com.github.loki.afro.metallum.core.parser.search.AbstractSearchParser;
import com.github.loki.afro.metallum.core.util.net.MetallumURL;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.search.AbstractSearchQuery;
import com.github.loki.afro.metallum.search.SearchRelevance;

import java.util.List;
import java.util.SortedMap;

public class MemberSearchQuery extends AbstractSearchQuery<Member> {

    public MemberSearchQuery() {
        super(new Member());
    }

    public MemberSearchQuery(final Member inputGeneric) {
        super(inputGeneric);
    }

    public void setMemberName(final String name) {
        this.searchObject.setName(name);
    }

    @Override
    protected final String assembleSearchQuery(final int startPage) {
        final String memberName = this.searchObject.getName();
        this.isAValidQuery = !memberName.isEmpty();
        return MetallumURL.assembleMemberSearchURL(this.searchObject.getName(), startPage);
    }

    @Override
    protected void setSpecialFieldsInParser(final AbstractSearchParser<Member> parser) {
        // there is nothing to set, because this is a simple query
    }

    @Override
    public void reset() {
        this.searchObject = new Member();
    }

    @Override
    protected SortedMap<SearchRelevance, List<Member>> enrichParsedEntity(final SortedMap<SearchRelevance, List<Member>> resultMap) {
        // nothing to enrich here
        return resultMap;
    }

}
