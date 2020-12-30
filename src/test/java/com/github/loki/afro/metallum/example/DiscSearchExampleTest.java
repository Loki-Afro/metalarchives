package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.query.DiscSearchQuery;
import com.github.loki.afro.metallum.search.service.advanced.DiscSearchService;
import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscSearchExampleTest {
    @Disabled
    public void test() throws MetallumException {
        DiscSearchService dss = new DiscSearchService();
        DiscSearchQuery dsq = new DiscSearchQuery();
        dsq.setBandName("Reverend Bizarre", false);
        // actually its a Split with Mannhai
        dsq.setReleaseName("Under the Sign of the Wolf", true);
        dsq.setCountries(Country.FINLAND);
        dsq.setLabel("The Church Within Records", false);
        dsq.setReleaseTypes(DiscType.SPLIT);
        dsq.setReleaseYearFrom(2006);
        dss.performSearch(dsq);
        assertThat(dss.isResultEmpty()).isFalse();
        // should be just one
        for (Disc disc : dss.getResultAsList()) {
            assertThat(disc.getName()).isEqualTo("Under the Sign of the Wolf");
            assertThat(disc.getId() == 135928).isTrue();
            assertThat(disc.isSplit()).isTrue();
            assertThat("[Reverend Bizarre [745], Mannhai [8915]]").isEqualTo(disc.getSplitBands().toString());
            assertThat(disc.getLabel().getName()).isEqualTo("The Church Within Records");
            assertThat(disc.getReleaseDate()).isEqualTo("November 4th, 2006");
        }
    }
}
