package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.LyricalThemesSearchQuery;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class LyricalThemesSearchTest {
    // TODO to test: -objectToLoad

    @Test
    public void noThemeTest() {
        final LyricalThemesSearchService searchService = new LyricalThemesSearchService();
        final LyricalThemesSearchQuery query = new LyricalThemesSearchQuery();
        query.setLyricalThemes("");
        try {
            searchService.performSearch(query);
            Assert.fail();
        } catch (MetallumException e) {
            Assert.assertFalse(e.getMessage().isEmpty());
        }
    }

    @Test
    public void genreTest() throws MetallumException {
        final LyricalThemesSearchService searchService = new LyricalThemesSearchService();
        final LyricalThemesSearchQuery query = new LyricalThemesSearchQuery();
        query.setLyricalThemes("Love Sex");
        final List<Band> result = searchService.performSearch(query);
        Assert.assertFalse(result.isEmpty());
        for (final Band band : result) {
            Assert.assertNotSame(0L, band.getId());
            Assert.assertFalse(band.getName().isEmpty());
            Assert.assertFalse(band.getGenre().isEmpty());
            Assert.assertFalse(band.getLyricalThemes().isEmpty());
            Assert.assertNotSame(Country.ANY, band.getCountry());
        }
    }
}
