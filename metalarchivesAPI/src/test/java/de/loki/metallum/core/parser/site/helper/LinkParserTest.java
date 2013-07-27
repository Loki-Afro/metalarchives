package de.loki.metallum.core.parser.site.helper;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import junit.framework.Assert;

import org.junit.Test;

import de.loki.metallum.entity.Link;

public class LinkParserTest {
	@Test
	public void simpleTest() throws ExecutionException {
		LinkParser parser = new LinkParser(6497L, LinkParser.MEMBER_PARSER);
		Link[] result = parser.parse();
		System.out.println(Arrays.toString(result));
	}

	public void illegalParameterTest() throws ExecutionException {
		try {
			new LinkParser(56L, -56);
		} catch (IllegalArgumentException e) {
			return;
		}
		Assert.fail();
	}
}
