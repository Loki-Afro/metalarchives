package de.loki.metallum.search.service;

import java.util.List;



import org.apache.log4j.Level;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.loki.metallum.MetallumException;
import de.loki.metallum.core.util.MetallumLogger;
import de.loki.metallum.entity.Band;
import de.loki.metallum.enums.Country;
import de.loki.metallum.search.query.LyricalThemesSearchQuery;

public class LyricalThemesSearchTest {
	// TODO to test: -objectToLoad

	@BeforeClass
	public static void setUpBeforeClass() {
		MetallumLogger.setLogLevel(Level.INFO);
	}

	@Test
	public void noThemesest() {
		final LyricalThemesSearchService searchService = new LyricalThemesSearchService();
		final LyricalThemesSearchQuery query = new LyricalThemesSearchQuery();
		query.setLyricalThemes("");
		try {
			searchService.performSearch(query);
			Assert.fail();
		} catch (MetallumException e) {
			Assert.assertFalse(e.getMessage().isEmpty());
		}
	}

	@Test
	public void genreTest() throws MetallumException {
		final LyricalThemesSearchService searchService = new LyricalThemesSearchService();
		final LyricalThemesSearchQuery query = new LyricalThemesSearchQuery();
		query.setLyricalThemes("Love Sex");
		final List<Band> result = searchService.performSearch(query);
		Assert.assertFalse(result.isEmpty());
		for (final Band band : result) {
			Assert.assertNotSame(0L, band.getId());
			Assert.assertFalse(band.getName().isEmpty());
			Assert.assertFalse(band.getGenre().isEmpty());
			Assert.assertFalse(band.getLyricalThemes().isEmpty());
			Assert.assertNotSame(Country.ANY, band.getCountry());
		}
	}
}
