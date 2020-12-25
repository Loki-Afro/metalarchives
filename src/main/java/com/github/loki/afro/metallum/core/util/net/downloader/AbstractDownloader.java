package com.github.loki.afro.metallum.core.util.net.downloader;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.ExponentialBackOff;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

abstract class AbstractDownloader {
    private final String urlString;
    private final static Logger logger = LoggerFactory.getLogger(AbstractDownloader.class);

    private static final String USER_AGENT_PROPERTY = "de.loki.metallum.useragent";
    private static final NetHttpTransport TRANSPORT = new NetHttpTransport.Builder().build();

    private static final List<Integer> retryResponseCodes = Lists.newArrayList(403, 520);
    private final HttpRequestFactory requestFactory;


    AbstractDownloader(final String urlString) {
        this.urlString = urlString;
        this.requestFactory = TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) {
                ExponentialBackOff backOff = new ExponentialBackOff.Builder()
                        .build();

                HttpBackOffUnsuccessfulResponseHandler unsuccessfulResponseHandler = new HttpBackOffUnsuccessfulResponseHandler(backOff);
                unsuccessfulResponseHandler.setBackOffRequired(response -> {
                    boolean isRetryCode = retryResponseCodes.contains(response.getStatusCode());
                    if (isRetryCode) {
                        logger.warn("retrying because of response status code: {}", response.getStatusCode());
                    }
                    return isRetryCode;
                });
                request.setUnsuccessfulResponseHandler(unsuccessfulResponseHandler);
            }
        });
    }

    final byte[] getDownloadEntity() throws IOException {

        logger.info("downloaded Content from " + this.urlString + " ...");

        HttpResponse response = null;
        try {
            response = createHttpRequest().execute();
            return getContentAsByteArray(response);
        } catch (HttpResponseException e) {
            throw new IOException(e);
        } finally {
            if (response != null) {
                response.disconnect();
            }
        }
    }

    private byte[] getContentAsByteArray(HttpResponse response) throws IOException {
        try (InputStream is = response.getContent()) {
            byte[] bytes = ByteStreams.toByteArray(is);
            logger.info("... download finished");
            return bytes;
        }
    }

    private HttpRequest createHttpRequest() throws IOException {
        HttpRequest httpRequest = this.requestFactory
                .buildGetRequest(new GenericUrl(this.urlString));

        httpRequest.getHeaders().setUserAgent(getUserAgent());
//        httpRequest.setNumberOfRetries(MAX_TRIES);

        return httpRequest;
    }

    private final String getUserAgent() {
        return System.getProperty(AbstractDownloader.USER_AGENT_PROPERTY, "");
    }
}
