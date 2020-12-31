package com.github.loki.afro.metallum.entity;

import com.github.loki.afro.metallum.entity.YearRange.Year;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;

import java.util.TreeSet;

import static com.github.loki.afro.metallum.entity.YearRange.Year.of;
import static com.github.loki.afro.metallum.entity.YearRange.Year.present;
import static org.assertj.core.api.Assertions.assertThat;

class YearRangeTest {

    @Test
    public void compareTo() {
        YearRange first = YearRange.of(of(1989), of(1990), "Ulceration", null);
        YearRange second = YearRange.of(of(1990), of(1990), "Fear the Factory", null);
        YearRange third = YearRange.of(of(1990), of(2002), "Fear Factory", 189L);
        YearRange last = YearRange.of(of(2020), present(), "Fear Factory", 189L);

        TreeSet<YearRange> objects  = Sets.newTreeSet(first, second, third, last);

        assertThat(objects).containsExactly(first, second, third, last);
    }
}