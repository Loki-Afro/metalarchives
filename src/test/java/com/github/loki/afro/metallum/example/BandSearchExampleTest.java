package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.BandSearchQuery;
import com.github.loki.afro.metallum.search.service.advanced.BandSearchService;
import org.junit.Assert;
import org.junit.Ignore;

public class BandSearchExampleTest {

    @Ignore
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
        Assert.assertFalse(bandSearchService.isResultEmpty());
        if (!bandSearchService.isResultEmpty()) {
            for (Band band : bandSearchService.getResultAsList()) {
                Assert.assertTrue(band.getCountry() == Country.SWEDEN);
                Assert.assertEquals(band.getName(), bandToSearchFor);
                Assert.assertEquals(band.getProvince(), "Stockholm");
                Assert.assertEquals(band.getLyricalThemes(), "Anti-Christ, Satan (old), Norse Mythology, Odinism");
                Assert.assertTrue(band.getId() == 184);
                Assert.assertEquals(band.getGenre(), "Black Metal, Viking, Thrash Metal");
                Assert.assertEquals(band.getLabel().getName(), "Black Mark Production");
                Assert.assertEquals(band.getLabel().getId(), 38);
                // here we could parse anything
                // BANDSTATUS
            }
        }
    }
}
