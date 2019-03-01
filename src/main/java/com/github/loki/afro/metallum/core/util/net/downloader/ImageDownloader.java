package com.github.loki.afro.metallum.core.util.net.downloader;

import com.github.loki.afro.metallum.core.util.net.downloader.interfaces.IImageDownloader;

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
        final byte[] imageInByte = getDownloadEntity();

        try (InputStream in = new ByteArrayInputStream(imageInByte)) {
            return ImageIO.read(in);
        }
    }

}
