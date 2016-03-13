package de.loki.metallum.search.service;

import java.util.List;



import org.junit.Assert;
import org.junit.Test;

import de.loki.metallum.MetallumException;
import de.loki.metallum.entity.Band;
import de.loki.metallum.enums.Country;
import de.loki.metallum.search.query.GenreSearchQuery;

public class GenreSearchServiceTest {
	// TODO to test: -objectToLoad

	@Test
	public void noGenreTest() {
		final GenreSearchService searchService = new GenreSearchService();
		final GenreSearchQuery query = new GenreSearchQuery();
		query.setGenre("");
		try {
			searchService.performSearch(query);
			Assert.fail();
		} catch (MetallumException e) {
			Assert.assertFalse(e.getMessage().isEmpty());
		}
	}

	@Test
	public void genreTest() throws MetallumException {
		final GenreSearchService searchService = new GenreSearchService();
		final GenreSearchQuery query = new GenreSearchQuery();
		query.setGenre("Symphonic Black Thrash Metal");
		final List<Band> result = searchService.performSearch(query);
		Assert.assertFalse(result.isEmpty());
		for (final Band band : result) {
			Assert.assertNotSame(0L, band.getId());
			Assert.assertFalse(band.getName().isEmpty());
			Assert.assertFalse(band.getGenre().isEmpty());
			Assert.assertNotSame(Country.ANY, band.getCountry());
		}
	}
}
