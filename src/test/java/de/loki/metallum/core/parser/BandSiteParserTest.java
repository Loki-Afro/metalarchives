package de.loki.metallum.core.parser;

import java.util.concurrent.ExecutionException;

import de.loki.metallum.core.parser.site.BandSiteParser;
import de.loki.metallum.entity.Band;

public class BandSiteParserTest extends AbstractSiteParserTest {
	@Override
	protected final Band getParsedEntity(final long id) throws ExecutionException {
		final Band band = new Band(id);
		final BandSiteParser parser = new BandSiteParser(band, true, true, true, true, true);
		return parser.parse();
	}

	@Override
	protected String getSubPackage() {
		return "band";
	}

	@Override
	protected String[] getFieldsToIgnore() {
		return new String[] { "similarArtist" };
	}
}
