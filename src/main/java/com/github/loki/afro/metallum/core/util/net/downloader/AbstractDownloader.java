package com.github.loki.afro.metallum.core.util.net.downloader;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

abstract class AbstractDownloader {
    private final String urlString;
    private final static Logger logger = LoggerFactory.getLogger(AbstractDownloader.class);

    private static final String USER_AGENT_PROPERTY = "de.loki.metallum.useragent";
    private static final HttpTransport httpTransport;

    static {
        httpTransport = new NetHttpTransport.Builder().build();
    }


    AbstractDownloader(final String urlString) {
        this.urlString = urlString;
    }

    final byte[] getDownloadEntity() throws IOException {
        HttpResponse response = getIntern();
        if (response.getStatusCode() == 403) {
            response = getIntern();
        }

        try (InputStream is = response.getContent();
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            logger.info("... download finished");
            return buffer.toByteArray();
        }


    }

    private HttpResponse getIntern() throws IOException {
        logger.info("downloaded Content from " + this.urlString + " ...");

        HttpRequest httpRequest = httpTransport.createRequestFactory()
                .buildGetRequest(new GenericUrl(this.urlString));
        httpRequest.getHeaders().setUserAgent(getUserAgent());

        return httpRequest.execute();
    }

    private final String getUserAgent() {
        return System.getProperty(AbstractDownloader.USER_AGENT_PROPERTY, "");
    }
}
