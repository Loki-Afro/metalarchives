package de.loki.metallum.core.util.net.downloader;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public final class ThreadExecuter extends ThreadPoolExecutor {
	private final static ThreadExecuter	instance	= new ThreadExecuter(4, 10);
	private static Logger				logger		= Logger.getLogger(ThreadExecuter.class);

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
