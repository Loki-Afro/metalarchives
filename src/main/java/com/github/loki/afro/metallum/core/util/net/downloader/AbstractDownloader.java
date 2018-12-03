package com.github.loki.afro.metallum.core.util.net.downloader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

abstract class AbstractDownloader {
    private final String urlString;
    private static final HttpClient HTTP_CLIENT;
    final static Charset HTML_CHARSET = Charset.forName("UTF-8");
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractDownloader.class);

    private static final String USER_AGENT_PROPERTY = "de.loki.metallum.useragent";

    static {
        CacheConfig cacheConfig = CacheConfig.custom()
                .setMaxCacheEntries(1000)
                .setHeuristicCachingEnabled(true)
                .build();

        HTTP_CLIENT = CachingHttpClientBuilder.create()
                .setCacheConfig(cacheConfig)
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .build();
    }

    AbstractDownloader(final String urlString) {
        this.urlString = urlString;
    }

    final HttpEntity getDownloadEntity() throws IOException {
        HttpResponse response = get();
        if (response.getStatusLine().getStatusCode() == 403) {
            response = get();
        }
        LOGGER.info("... download finished");
        return response.getEntity();
    }

    private HttpResponse get() throws IOException {
        LOGGER.info("downloaded Content from " + this.urlString + " ...");
        final HttpGet request = new HttpGet(this.urlString);
        request.addHeader("User-Agent", getUserAgent());
        return HTTP_CLIENT.execute(request);
    }

    private final String getUserAgent() {
        return System.getProperty(AbstractDownloader.USER_AGENT_PROPERTY, "");
    }
}
