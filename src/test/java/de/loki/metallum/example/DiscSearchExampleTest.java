package de.loki.metallum.example;

import org.junit.Assert;
import org.junit.Ignore;

import de.loki.metallum.MetallumException;
import de.loki.metallum.entity.Disc;
import de.loki.metallum.enums.Country;
import de.loki.metallum.enums.DiscType;
import de.loki.metallum.search.query.DiscSearchQuery;
import de.loki.metallum.search.service.advanced.DiscSearchService;

public class DiscSearchExampleTest {
	@Ignore
	public void test() throws MetallumException {
		DiscSearchService dss = new DiscSearchService();
		DiscSearchQuery dsq = new DiscSearchQuery();
		dsq.setBandName("Reverend Bizarre", false);
		// actually its a Split with Mannhai
		dsq.setReleaseName("Under the Sign of the Wolf", true);
		dsq.setCountrys(Country.FINLAND);
		dsq.setLabel("The Church Within Records", false);
		dsq.setReleaseTypes(DiscType.SPLIT);
		dsq.setReleaseYearFrom(2006);
		dss.performSearch(dsq);
		Assert.assertFalse(dss.isResultEmpty());
		if (!dss.isResultEmpty()) {
			// should be just one
			for (Disc disc : dss.getResultAsList()) {
				Assert.assertEquals(disc.getName(), "Under the Sign of the Wolf");
				Assert.assertTrue(disc.getId() == 135928);
				Assert.assertTrue(disc.isSplit());
				Assert.assertEquals("[Reverend Bizarre [745], Mannhai [8915]]", disc.getSplitBands().toString());
				Assert.assertEquals(disc.getLabel().getName(), "The Church Within Records");
				Assert.assertEquals(disc.getReleaseDate(), "November 4th, 2006");
			}
		}
	}
}
