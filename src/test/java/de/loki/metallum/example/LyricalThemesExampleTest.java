package de.loki.metallum.example;

import java.util.ArrayList;
import java.util.List;


import org.junit.Assert;
import org.junit.Ignore;

import de.loki.metallum.MetallumException;
import de.loki.metallum.search.query.LyricalThemesSearchQuery;
import de.loki.metallum.search.service.LyricalThemesSearchService;

public class LyricalThemesExampleTest {

	@Ignore
	public void test() throws MetallumException {

		List<String> stringList = new ArrayList<String>();
		stringList.add("Love");
		stringList.add("Sex");
		for (String lyricalThemes : stringList) {
			LyricalThemesSearchQuery query = new LyricalThemesSearchQuery();
			LyricalThemesSearchService service = new LyricalThemesSearchService();
			query.setLyricalThemes(lyricalThemes);
			service.performSearch(query);
			Assert.assertFalse(service.getResultAsList().isEmpty());
		}

	}

}
