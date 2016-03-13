package de.loki.metallum.core.util.net.downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public final class ThreadExecuter extends ThreadPoolExecutor {
	private final static ThreadExecuter instance = new ThreadExecuter(4, 10);
	private static       Logger         logger   = LoggerFactory.getLogger(ThreadExecuter.class);

	private ThreadExecuter(final int corePoolSize, final int maximumPoolSize) {
		super(corePoolSize, maximumPoolSize, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		allowCoreThreadTimeOut(true);
	}

	protected static ThreadExecuter getInstance() {
		return instance;
	}

	@Override
	public <T> Future<T> submit(final Callable<T> task) {
		logger.debug("Task count: " + getTaskCount());
		logger.debug("Queue size: " + getQueue().size());
		logger.debug("completedTaskCount: " + getCompletedTaskCount());
		logger.debug("Remaining Queue Capazity: " + getQueue().remainingCapacity());
		return super.submit(task);
	}
}
