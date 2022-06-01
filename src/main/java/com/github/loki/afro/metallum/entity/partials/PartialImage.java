package com.github.loki.afro.metallum.entity.partials;

import com.github.loki.afro.metallum.core.util.net.downloader.Downloader;

public class PartialImage extends Partial<byte[]> {
    private final String url;

    public PartialImage(String url) {
        this.url = url;
    }

    @Override
    public byte[] load() {
        return Downloader.getImage(this.url);
    }
}
