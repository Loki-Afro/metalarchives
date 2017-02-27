package com.github.loki.afro.metallum.core.util;


import org.junit.Assert;
import org.junit.Test;

public class MetallumUtilTest {
    @Test
    public void isStringInArrayTest() {
        Assert.assertTrue(MetallumUtil.isStringInArray("Graupel", "Sturm", "Donner", "Graupel"));
        Assert.assertFalse(MetallumUtil.isStringInArray("Graupel", "Sturm", "Donner"));
    }

    @Test
    public void testIsMetallumOnline() {
        Assert.assertTrue(MetallumUtil.isEncyclopediaMetallumOnline());
    }
}
