package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.search.query.entity.SearchTrackResult;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.query.entity.TrackQuery;
import com.github.loki.afro.metallum.search.service.advanced.TrackSearchService;
import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.Assertions.assertThat;

public class TrackSearchExampleTest {

    @Disabled
    public void test() throws MetallumException {
        TrackSearchService trackService = new TrackSearchService(true);
        // This is a song which was covered by many many bands
        TrackQuery query = TrackQuery.builder()
                .name("Gate of Nanna")
                .bandName("Beherit")
                .discType(DiscType.DEMO)
                .build();

        for (SearchTrackResult track : trackService.get(query)) {
            assertThat(track.getBandName()).isEqualTo("Beherit");
            assertThat(track.getDiscName()).isEqualTo("Promo 1992");
            assertThat(track.getName()).isEqualTo("The Gate of Nanna");
            // lyrics will come
        }
    }

}
