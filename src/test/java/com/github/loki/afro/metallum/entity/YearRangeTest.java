package com.github.loki.afro.metallum.entity;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;

import java.util.TreeSet;

import static com.github.loki.afro.metallum.entity.YearRange.Year.of;
import static com.github.loki.afro.metallum.entity.YearRange.Year.present;
import static org.assertj.core.api.Assertions.assertThat;

class YearRangeTest {

    private static final YearRange first = YearRange.of(of(1989), of(1990), "Ulceration", null);
    private static final YearRange second = YearRange.of(of(1990), of(1990), "Fear the Factory", null);
    private static final YearRange third = YearRange.of(of(1990), of(2002), "Fear Factory", 189L);
    private static final YearRange last = YearRange.of(of(2020), present(), "Fear Factory", 189L);

    @Test
    public void compareTo() {
        TreeSet<YearRange> objects = Sets.newTreeSet(first, second, third, last);

        assertThat(objects).containsExactly(first, second, third, last);
    }

    @Test
    public void testToString() {
        assertThat(first.toString()).isEqualTo("1989-1990 (as Ulceration)");
        assertThat(second.toString()).isEqualTo("1990 (as Fear the Factory)");
        assertThat(third.toString()).isEqualTo("1990-2002 (as Fear Factory$189)");
        assertThat(last.toString()).isEqualTo("2020-present (as Fear Factory$189)");
    }
}