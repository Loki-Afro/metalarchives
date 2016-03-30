package de.loki.metallum.core.util.net.downloader;

import de.loki.metallum.core.util.net.downloader.interfaces.IContentDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Downloader {

	private final static ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(4, 10, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()) {
		{
			allowCoreThreadTimeOut(true);
		}
	};

	private static final Logger LOGGER = LoggerFactory.getLogger(Downloader.class);

	public static BufferedImage getImage(final String urlString) throws ExecutionException {
		return get(urlString, new ImageDownloader(urlString));
	}

	public static String getHTML(final String urlString) throws ExecutionException {
		return get(urlString, new HTMLDownloader(urlString));
	}

	private static <T> T get(final String request, final IContentDownloader<T> downloader) throws ExecutionException {
		try {
			LOGGER.debug("Task count: " + THREAD_POOL_EXECUTOR.getTaskCount());
			LOGGER.debug("Queue size: " + THREAD_POOL_EXECUTOR.getQueue().size());
			LOGGER.debug("completedTaskCount: " + THREAD_POOL_EXECUTOR.getCompletedTaskCount());
			LOGGER.debug("Remaining Queue Capacity: " + THREAD_POOL_EXECUTOR.getQueue().remainingCapacity());
			return THREAD_POOL_EXECUTOR.submit(downloader).get();
		} catch (final InterruptedException e) {
			LOGGER.error("Failed to download: " + request, e);
		}
		return null;
	}

}
