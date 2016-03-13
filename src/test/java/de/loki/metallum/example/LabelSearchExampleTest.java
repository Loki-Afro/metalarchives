package de.loki.metallum.example;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;

import de.loki.metallum.MetallumException;
import de.loki.metallum.search.query.LabelSearchQuery;
import de.loki.metallum.search.service.LabelSearchService;

public class LabelSearchExampleTest {

	@Ignore
	public void test() throws MetallumException {
		List<String> stringList = new ArrayList<String>();
		stringList.add("Deathlike Silence");
		stringList.add("Apparitia Recordings");
		stringList.add("Metal Blade Records");
		stringList.add("Nuclear");
		stringList.add("Century Media Records");
		stringList.add("");
		for (String labelName : stringList) {
			// System.out.println("searching with: " + labelName);
			LabelSearchQuery query = new LabelSearchQuery();
			LabelSearchService service = new LabelSearchService();
			query.setLabelName(labelName);
			service.performSearch(query);
			Assert.assertFalse(service.getResultAsList().isEmpty());
		}

	}

}
