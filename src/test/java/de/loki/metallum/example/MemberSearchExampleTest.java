package de.loki.metallum.example;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;

import de.loki.metallum.MetallumException;
import de.loki.metallum.search.query.MemberSearchQuery;
import de.loki.metallum.search.service.MemberSearchService;

public class MemberSearchExampleTest {

	@Ignore
	public void test() throws MetallumException {

		List<String> stringList = new ArrayList<String>();
		stringList.add("Kerry King");
		stringList.add("");
		stringList.add("Fenriz");
		stringList.add("Bruce Dickinson");
		stringList.add("Mick Kenney");
		stringList.add("Joel Lindholm");
		stringList.add("Varg Vikernes");
		for (String memberName : stringList) {
			// System.out.println("searching with: " + memberName);
			MemberSearchQuery query = new MemberSearchQuery();
			MemberSearchService service = new MemberSearchService();
			query.setMemberName(memberName);
			service.performSearch(query);
			Assert.assertFalse(service.getResultAsList().isEmpty());
		}

	}
}
