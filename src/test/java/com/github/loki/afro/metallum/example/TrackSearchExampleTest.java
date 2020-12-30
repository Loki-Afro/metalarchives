package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.query.TrackSearchQuery;
import com.github.loki.afro.metallum.search.service.advanced.TrackSearchService;
import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.Assertions.assertThat;

public class TrackSearchExampleTest {

    @Disabled
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
            assertThat(track.getBand().getName()).isEqualTo("Beherit");
            assertThat(track.getDiscOfThisTrack().getName()).isEqualTo("Promo 1992");
            assertThat(track.getName()).isEqualTo("The Gate of Nanna");
            // lyrics will come
        }
        assertThat(trackService.isResultEmpty()).isFalse();
    }

}
