package com.github.loki.afro.metallum.search.service;

import com.github.loki.afro.metallum.enums.Country;
import com.github.loki.afro.metallum.search.query.entity.BandByModificationDateResult;
import com.github.loki.afro.metallum.search.query.entity.DateQuery;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

public class BandsByModificationDateServiceTest {

    @Test
    public void test() {
        DateQuery query = new DateQuery(Month.JUNE, Year.of(2020));
        Iterable<BandByModificationDateResult> bandByModificationDateResults = new BandsByModificationDateService().get(query);

        BandByModificationDateResult sample = bandByModificationDateResults.iterator().next();

        assertThat(sample.getId()).isEqualTo(16110L);
        assertThat(sample.getName()).isEqualTo("Vardøger");
        assertThat(sample.getCountry()).isEqualTo(Country.NO);
        assertThat(sample.getGenre()).isEqualTo("Melodic Black Metal (early); Progressive/Melodic Death Metal (later)");
        assertThat(sample.getModifiedOn()).isEqualTo("Jun 30th, 23:59");
    }

}