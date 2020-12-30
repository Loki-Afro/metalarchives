package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.LyricalThemesSearchQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LyricalThemesSearchTest {
    // TODO to test: -objectToLoad

    @Test
    public void noThemeTest() {
        final LyricalThemesSearchService searchService = new LyricalThemesSearchService();
        final LyricalThemesSearchQuery query = new LyricalThemesSearchQuery();
        query.setLyricalThemes("");

        assertThatThrownBy(() -> searchService.performSearch(query)).isInstanceOf(MetallumException.class);
    }

    @Test
    public void genreTest() throws MetallumException {
        final LyricalThemesSearchService searchService = new LyricalThemesSearchService();
        final LyricalThemesSearchQuery query = new LyricalThemesSearchQuery();
        query.setLyricalThemes("Love Sex");
        final List<Band> result = searchService.performSearch(query);
        assertThat(result).isNotEmpty();
        for (final Band band : result) {
            assertThat(band.getId()).isNotEqualTo(0L);
            assertThat(band.getName()).isNotEmpty();
            assertThat(band.getGenre()).isNotEmpty();
            assertThat(band.getLyricalThemes()).isNotEmpty();
            assertThat(band.getCountry()).isNotEqualTo(Country.ANY);
        }
    }
}
