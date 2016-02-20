package de.loki.metallum.core.parser;

import java.util.concurrent.ExecutionException;

import de.loki.metallum.core.parser.site.LabelSiteParser;
import de.loki.metallum.core.parser.site.LabelSiteParser.PARSE_STYLE;
import de.loki.metallum.entity.Label;

public class LabelSiteParserTest extends AbstractSiteParserTest {

	@Override
	protected String getSubPackage() {
		return "label";
	}

	@Override
	protected Label getParsedEntity(final long id) throws ExecutionException {
		final Label label = new Label(id);
		final LabelSiteParser parser = new LabelSiteParser(label, true, true, PARSE_STYLE.BAND_SEARCH_MODE, PARSE_STYLE.BAND_SEARCH_MODE, PARSE_STYLE.BAND_SEARCH_MODE);
		return parser.parse();
	}

}
