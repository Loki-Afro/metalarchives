package de.loki.metallum.example;

import org.junit.Assert;
import org.junit.Ignore;

import de.loki.metallum.MetallumException;
import de.loki.metallum.entity.Track;
import de.loki.metallum.enums.DiscType;
import de.loki.metallum.search.query.TrackSearchQuery;
import de.loki.metallum.search.service.advanced.TrackSearchService;

public class TrackSearchExampleTest {

	@Ignore
	public void test() throws MetallumException {
		TrackSearchQuery trackQuery = new TrackSearchQuery();
		TrackSearchService trackService = new TrackSearchService(true);
		// This is a song which was covered by many many bands
		trackQuery.setSongTitle("Gate of Nanna", false);
		trackQuery.setBandName("Beherit", false);
		trackQuery.setDiscType(DiscType.DEMO);
		trackService.performSearch(trackQuery);
		trackService.getResultAsList();
		for (Track track : trackService.getResultAsList()) {
			Assert.assertEquals("Beherit", track.getBand().getName());
			Assert.assertEquals("Promo 1992", track.getDiscOfThisTrack().getName());
			Assert.assertEquals("The Gate of Nanna", track.getName());
			// lyrics will come
		}
		Assert.assertFalse(trackService.isResultEmpty());
	}

}
