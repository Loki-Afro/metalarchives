package de.loki.metallum.core.util.net.downloader;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import de.loki.metallum.core.util.net.downloader.interfaces.IImageDownloader;

public final class ImageDownloader extends AbstractDownloader implements IImageDownloader {

	protected ImageDownloader(String urlString) {
		super(urlString);
	}

	@Override
	public final BufferedImage call() throws Exception {
		final HttpEntity entity = getDownloadEntity();
		final byte[] imageInByte = EntityUtils.toByteArray(entity);
		final InputStream in = new ByteArrayInputStream(imageInByte);
		final BufferedImage image = ImageIO.read(in);
		in.close();
		return image;
	}

}
