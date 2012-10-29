package de.loki.metallum.core.util.net.downloader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;

import org.apache.log4j.Level;
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.loki.metallum.core.util.MetallumLogger;

public class DownloaderTest {

	@BeforeClass
	public static void setUpBeforeClass() {
		MetallumLogger.setLogLevel(Level.INFO);
	}

	@Test
	public void downloadImage() throws IOException, ExecutionException {
		final BufferedImage testImage = Downloader.getImage("http://i.imgur.com/4Hkmk.png");
		final BufferedImage controlimage = ImageIO.read(getClass().getClassLoader().getResource("de/loki/metallum/core/util/net/downloader/gnu.png"));
		Assert.assertTrue(compareImages(controlimage, testImage));
	}

	private boolean compareImages(final BufferedImage controlImage, final BufferedImage testImage) {
		final int columns = controlImage.getWidth();
		final int rows = controlImage.getHeight();

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				int controlRgb = controlImage.getRGB(col, row);
				int testRgb = testImage.getRGB(col, row);
				if (controlRgb != testRgb) {
					return false;
				}
			}
		}
		return true;
	}

	@Test
	public void downloadHtml() throws URISyntaxException, ExecutionException {
		final String downloadedHtml = Downloader.getHTML("http://pastebin.com/raw.php?i=iEjw3swD");
		Assert.assertEquals("<html><head><title>Riesenzwerg</title></head><body><h1>Hallo Welt!</h1><strong>Fett</strong><em>Kursiv</em></body></html>", downloadedHtml);
		Assert.assertEquals("Riesenzwerg Hallo Welt!FettKursiv", Jsoup.parse(downloadedHtml).text());
	}

	@Test
	public void downloadExceptionTest() throws URISyntaxException {
		try {
			Downloader.getHTML("http://past7878787877878787ebin.com/raw.php?i=iEjw3swD");
		} catch (ExecutionException e) {
			Assert.assertNotNull(e);
		}
	}

}
