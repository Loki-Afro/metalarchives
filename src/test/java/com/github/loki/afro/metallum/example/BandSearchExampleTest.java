package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.enums.BandStatus;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.github.loki.afro.metallum.search.service.advanced.BandSearchService;
import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.Assertions.assertThat;

public class BandSearchExampleTest {

    @Disabled
    public void test() throws MetallumException {
        String bandToSearchFor = "Bathory";
        BandSearchService bandSearchService = new BandSearchService();
        bandSearchService.setLoadSimilar(true);

        BandQuery query = BandQuery.builder()
                .name(bandToSearchFor)
                .exactBandNameMatch(true)
                .genre("Black Metal")
                .labelName("*")
                .yearOfFormationFromYear(1970)
                .province("*")
                .lyricalThemes("*")
                .country(Country.SE)
                .status(BandStatus.ON_HOLD)
                .build();


        for (SearchBandResult band : bandSearchService.get(query)) {
            assertThat(band.getCountry()).contains(Country.SE);
            assertThat(band.getName()).isEqualTo(bandToSearchFor);
            assertThat(band.getProvince()).contains("Stockholm");
            assertThat(band.getLyricalThemes()).contains("Anti-Christ, Satan (old), Norse Mythology, Odinism");
            assertThat(band.getId()).isEqualTo(184L);
            assertThat(band.getGenre()).contains("Black Metal, Viking, Thrash Metal");
            assertThat(band.getLabelName()).isEqualTo("Black Mark Production");
            assertThat(band.getLabelId()).isEqualTo(38);
            // here we could parse anything
        }
    }
}
