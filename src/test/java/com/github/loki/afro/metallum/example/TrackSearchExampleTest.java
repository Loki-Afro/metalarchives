package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.query.TrackSearchQuery;
import com.github.loki.afro.metallum.search.service.advanced.TrackSearchService;
import org.junit.Assert;
import org.junit.Ignore;

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
