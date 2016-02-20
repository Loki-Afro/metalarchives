package de.loki.metallum.core.util;

import junit.framework.Assert;

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
