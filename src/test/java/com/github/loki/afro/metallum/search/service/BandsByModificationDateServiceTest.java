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
//        we can't check for any specific band here, it turns out that they get removed as soon as they get updated again
//        with other words its actually what changed in this time period that got not updated again

        assertThat(sample.getId()).isNotEqualTo(0L);
        assertThat(sample.getName()).isNotBlank();
        assertThat(sample.getCountry()).isNotNull();
        assertThat(sample.getGenre()).isNotBlank();
        assertThat(sample.getModifiedOn()).startsWith("Jun ");
    }

}