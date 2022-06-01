package com.github.loki.afro.metallum.core.util.net.downloader;

import com.github.loki.afro.metallum.core.util.net.downloader.interfaces.IContentDownloader;

final class ImageDownloader extends AbstractDownloader implements IContentDownloader<byte[]> {

    ImageDownloader(final String urlString) {
        super(urlString);
    }

    @Override
    public byte[] call() throws Exception {
        return getDownloadEntity();
    }

}
