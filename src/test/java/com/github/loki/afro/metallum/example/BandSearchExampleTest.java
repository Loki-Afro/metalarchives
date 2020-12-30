package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.BandSearchQuery;
import com.github.loki.afro.metallum.search.service.advanced.BandSearchService;
import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.Assertions.assertThat;

public class BandSearchExampleTest {

    @Disabled
    public void test() throws MetallumException {
        String bandToSearchFor = "Bathory";
        BandSearchService bandSearchService = new BandSearchService();
        bandSearchService.setLoadSimilar(true);
        BandSearchQuery bandSearchQuery = new BandSearchQuery();
        bandSearchQuery.setBandName(bandToSearchFor, true);
        bandSearchQuery.setGenre("Black Metal");
        bandSearchQuery.setLabel("*", false);
        bandSearchQuery.setYearOfFormationFrom(1970);
        bandSearchQuery.setProvince("*", "*");
        bandSearchQuery.setLyricalThemes("*");
        bandSearchService.performSearch(bandSearchQuery);
        assertThat(bandSearchService.isResultEmpty()).isFalse();

        for (Band band : bandSearchService.getResultAsList()) {
            assertThat(band.getCountry()).isEqualTo(Country.SWEDEN);
            assertThat(band.getName()).isEqualTo(bandToSearchFor);
            assertThat(band.getProvince()).isEqualTo("Stockholm");
            assertThat(band.getLyricalThemes()).isEqualTo("Anti-Christ, Satan (old), Norse Mythology, Odinism");
            assertThat(band.getId()).isEqualTo(184L);
            assertThat(band.getGenre()).isEqualTo("Black Metal, Viking, Thrash Metal");
            assertThat(band.getLabel().getName()).isEqualTo("Black Mark Production");
            assertThat(band.getLabel().getId()).isEqualTo(38);
            // here we could parse anything
        }
    }
}
