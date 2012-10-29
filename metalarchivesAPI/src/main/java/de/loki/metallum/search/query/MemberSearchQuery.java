package de.loki.metallum.search.query;

import java.util.List;
import java.util.SortedMap;

import de.loki.metallum.core.parser.search.AbstractSearchParser;
import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.entity.Member;
import de.loki.metallum.search.AbstractSearchQuery;
import de.loki.metallum.search.SearchRelevance;

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
