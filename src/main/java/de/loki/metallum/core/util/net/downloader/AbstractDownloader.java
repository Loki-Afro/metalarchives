package de.loki.metallum.core.util.net.downloader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

abstract class AbstractDownloader {
	private final             String     urlString;
	protected volatile static HttpClient HTTP_CLIENT;
	protected final static Charset HTML_CHARSET = Charset.forName("UTF-8");
	private final static   int     PORT         = 80;
	private final static   String  PROTOCOL     = "http";
	private static         Logger  logger       = LoggerFactory.getLogger(AbstractDownloader.class);

	private static final String USER_AGENT_PROPERTY = "de.loki.metallum.useragent";

	static {
		final CacheConfig cacheConfig = new CacheConfig();
		cacheConfig.setMaxCacheEntries(1000);
		cacheConfig.setHeuristicCachingEnabled(true);

		final HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		final SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme(PROTOCOL, PORT, PlainSocketFactory.getSocketFactory()));
		HTTP_CLIENT = new CachingHttpClient(new DefaultHttpClient(new PoolingClientConnectionManager(schemeRegistry), params), cacheConfig);
	}

	protected AbstractDownloader(final String urlString) {
		this.urlString = urlString;
	}

	protected final HttpEntity getDownloadEntity() throws Exception {
		logger.info("downloaded Content from " + this.urlString + " ...");
		final HttpGet request = new HttpGet(this.urlString);
		request.addHeader("User-Agent", getUserAgent());
		final HttpResponse response = HTTP_CLIENT.execute(request);
		logger.info("... download finished");
		return response.getEntity();
	}

	private final String getUserAgent() {
		return System.getProperty(AbstractDownloader.USER_AGENT_PROPERTY, "");
	}
}
