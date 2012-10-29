package de.loki.metallum.core.parser;

import java.util.concurrent.ExecutionException;

import de.loki.metallum.core.parser.site.MemberSiteParser;
import de.loki.metallum.entity.Member;

public class MemberSiteParserTest extends AbstractSiteParserTest {
	@Override
	protected final Member getParsedEntity(final long id) throws ExecutionException {
		final Member member = new Member(id);
		final MemberSiteParser parser = new MemberSiteParser(member, true, true, true);
		return parser.parse();
	}

	@Override
	protected String getSubPackage() {
		return "member";
	}
}
