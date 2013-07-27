package de.loki.metallum.core.parser.site.helper.band;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import de.loki.metallum.entity.Band;

public class SimilarArtistsParserTest {

	@Test
	public void simpleTest() throws ExecutionException {
		SimilarArtistsParser parser = new SimilarArtistsParser(10936L);
		Map<Integer, List<Band>> result = parser.parse();
		Assert.assertFalse(result.isEmpty());
	}

}
