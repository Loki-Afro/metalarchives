package de.loki.metallum.core.parser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.examples.RecursiveElementNameAndTextQualifier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;

import de.loki.metallum.entity.AbstractEntity;

public abstract class AbstractSiteParserTest {

	private List<File>	xmlFiles;

	protected abstract String getSubPackage();

	@Before
	public void create() {
		XMLUnit.setIgnoreAttributeOrder(true);
		final File file;
		ClassLoader classLoader = getClass().getClassLoader();
		try {
			file = new File(classLoader.getResource("de/loki/metallum/core/parser/" + getSubPackage()).toURI());
			this.xmlFiles = addFiles(null, file);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public List<File> addFiles(List<File> files, final File dir) {
		if (files == null) {
			files = new LinkedList<File>();
		}

		if (!dir.isDirectory()) {
			files.add(dir);
			return files;
		}

		for (final File file : dir.listFiles()) {
			addFiles(files, file);
		}
		return files;
	}

	@Test
	@Ignore
	public void test() throws InterruptedException, ExecutionException {
		final ExecutorService es = Executors.newCachedThreadPool();
		List<Future<Boolean>> booleanList = new CopyOnWriteArrayList<Future<Boolean>>();
		for (final File file : this.xmlFiles) {
			booleanList.add(es.submit(new TestRunner(file)));
		}
		es.shutdown();
		while (!es.isTerminated()) {
			Thread.sleep(1000);
		}

		for (final Future<Boolean> future : booleanList) {
			Assert.assertTrue(future.get());
		}
	}

	protected String[] getFieldsToIgnore() {
		return new String[0];
	}

	protected abstract AbstractEntity getParsedEntity(long id) throws ExecutionException;

	private class TestRunner implements Callable<Boolean> {

		private final File		xmlFile;
		private AbstractEntity	controlEntity;

		private TestRunner(final File xmlFile) {
			this.xmlFile = xmlFile;
		}

		@Override
		public Boolean call() throws Exception {
			this.controlEntity = (AbstractEntity) new XStream().fromXML(this.xmlFile);
			final AbstractEntity testEntity = getParsedEntity(this.controlEntity.getId());

			final String[] ignoreFields = getFieldsToIgnore();
			try {
				return compare(this.controlEntity.toXml(ignoreFields), testEntity.toXml(ignoreFields));
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;

		}

		@SuppressWarnings("unchecked")
		private boolean compare(final String control, final String test) throws SAXException, IOException {
			final Diff diff = new Diff(control, test);
			diff.overrideElementQualifier(new RecursiveElementNameAndTextQualifier());

			if (!diff.similar()) {
				System.err.println("Test failed: " + this.controlEntity.getName());
				final DetailedDiff detDiff = new DetailedDiff(diff);
				List<Difference> differences = detDiff.getAllDifferences();
				for (Difference difference : differences) {
					System.err.println("****************************");
					System.err.println(difference);
					System.err.println("****************************");
				}
			}
			return diff.similar();
		}

	}
}
