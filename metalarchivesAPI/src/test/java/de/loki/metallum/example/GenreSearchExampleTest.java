package de.loki.metallum.example;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;

import de.loki.metallum.MetallumException;
import de.loki.metallum.search.query.GenreSearchQuery;
import de.loki.metallum.search.service.GenreSearchService;

public class GenreSearchExampleTest {

	@Ignore
	public void test() throws MetallumException {

		List<String> stringList = new ArrayList<String>();
		stringList.add("Death Metal");
		stringList.add("Death Metal");
		// stringList.add("Black Metal");
		for (String genreName : stringList) {
			GenreSearchQuery query = new GenreSearchQuery();
			GenreSearchService service = new GenreSearchService();
			service.setObjectsToLoad(5);
			query.setGenre(genreName);
			service.performSearch(query);
			Assert.assertFalse(service.getResultAsList().isEmpty());
		}

	}
}
