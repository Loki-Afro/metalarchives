package com.github.loki.afro.metallum.example;

import com.github.loki.afro.metallum.MetallumException;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import org.junit.jupiter.api.Disabled;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscSearchExampleTest {
    @Disabled
    public void test() throws MetallumException {
        Iterable<Disc> result = API.getDiscsFully(DiscQuery.builder()
                .bandName("Reverend Bizarre")
                .name("Under the Sign of the Wolf")
                .exactNameMatch(true)
                .country(Country.FI)
                .labelName("The Church Within Records")
                .discType(DiscType.SPLIT)
                .fromYear(2006)
                .build());

        // should be just one
        for (Disc disc : result) {
            assertThat(disc.getName()).isEqualTo("Under the Sign of the Wolf");
            assertThat(disc.getId() == 135928).isTrue();
            assertThat(disc.isSplit()).isTrue();
            assertThat("[Reverend Bizarre [745], Mannhai [8915]]").isEqualTo(disc.getSplitBands().toString());
            assertThat(disc.getLabel().getName()).isEqualTo("The Church Within Records");
            assertThat(disc.getReleaseDate()).isEqualTo("November 4th, 2006");
        }
    }
}
