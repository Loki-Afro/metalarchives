package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.GenreSearchQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GenreSearchServiceTest {
    // TODO to test: -objectToLoad

    @Test
    public void noGenreTest() {
        final GenreSearchService searchService = new GenreSearchService();
        final GenreSearchQuery query = new GenreSearchQuery();
        query.setGenre("");
        assertThatThrownBy(() -> searchService.performSearch(query)).isInstanceOf(MetallumException.class);
    }

    @Test
    public void genreTest() throws MetallumException {
        final GenreSearchService searchService = new GenreSearchService();
        final GenreSearchQuery query = new GenreSearchQuery();
        query.setGenre("Symphonic Black Thrash Metal");
        final List<Band> result = searchService.performSearch(query);
        assertThat(result).isNotEmpty();
        for (final Band band : result) {
            assertThat(band.getId()).isNotEqualTo(0L);
            assertThat(band.getName()).isNotEmpty();
            assertThat(band.getGenre()).isNotEmpty();
            assertThat(band.getCountry()).isNotEqualTo(Country.ANY);
        }
    }
}
