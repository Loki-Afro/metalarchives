package com.github.loki.afro.metallum.core.util.net.downloader;

import com.github.loki.afro.metallum.core.util.net.downloader.interfaces.IContentDownloader;

import java.nio.charset.StandardCharsets;

final class HTMLDownloader extends AbstractDownloader implements IContentDownloader<String> {

    HTMLDownloader(final String urlString) {
        super(urlString);
    }

    @Override
    public final String call() throws Exception {
        return new String(getDownloadEntity(), StandardCharsets.UTF_8);
    }

}
