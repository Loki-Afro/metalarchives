package de.loki.metallum.core.util.net.downloader;

import de.loki.metallum.core.util.net.downloader.interfaces.IImageDownloader;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

final class ImageDownloader extends AbstractDownloader implements IImageDownloader {

	ImageDownloader(final String urlString) {
		super(urlString);
	}

	@Override
	public final BufferedImage call() throws Exception {
		final HttpEntity entity = getDownloadEntity();
		final byte[] imageInByte = EntityUtils.toByteArray(entity);
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(imageInByte);
			return ImageIO.read(in);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

}
