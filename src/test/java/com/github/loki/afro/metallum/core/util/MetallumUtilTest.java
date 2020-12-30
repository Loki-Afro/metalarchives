package com.github.loki.afro.metallum.core.util;



import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MetallumUtilTest {
    @Test
    public void isStringInArrayTest() {
        assertThat(MetallumUtil.isStringInArray("Graupel", "Sturm", "Donner", "Graupel")).isTrue();
        assertThat(MetallumUtil.isStringInArray("Graupel", "Sturm", "Donner")).isFalse();
    }
}
