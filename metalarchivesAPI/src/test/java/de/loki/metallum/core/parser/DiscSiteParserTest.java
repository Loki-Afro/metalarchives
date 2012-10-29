package de.loki.metallum.core.parser;

import java.util.concurrent.ExecutionException;

import de.loki.metallum.core.parser.site.DiscSiteParser;
import de.loki.metallum.entity.Disc;

public class DiscSiteParserTest extends AbstractSiteParserTest {

	@Override
	protected final Disc getParsedEntity(final long id) throws ExecutionException {
		final Disc disc = new Disc(id);
		final DiscSiteParser parser = new DiscSiteParser(disc, true, true, true);
		return parser.parse();
	}

	@Override
	protected String getSubPackage() {
		return "album";
	}

}
