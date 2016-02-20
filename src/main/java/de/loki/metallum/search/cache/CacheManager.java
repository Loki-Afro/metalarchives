package de.loki.metallum.search.cache;

import org.apache.log4j.Logger;

public class CacheManager {

	public static final String		CACHE_IMPLEMENTATION_PROPERTY	= "de.loki.metallum.cacheimpl";
	private static volatile ICache	icacheImpl;
	private static final Logger		logger							= Logger.getLogger(CacheManager.class);

	public final static ICache getInstance() {
		if (icacheImpl == null) {
			newInstance();
		}
		return icacheImpl;
	}

	private final static synchronized void newInstance() {
		try {
			if (icacheImpl != null) {
				return;
			}
			final String classImpl = System.getProperty(CacheManager.CACHE_IMPLEMENTATION_PROPERTY, ICache.DEFAULT_CACHE);
			final ICache iCache = (ICache) Class.forName(classImpl).newInstance();
			icacheImpl = iCache;
			logger.info("Using Cacheimpl: " + icacheImpl.getClass().getName());
		} catch (Exception e) {
			logger.error("NO CACHE IMPLMENTATION FOUND!", e);
		}
	}
}
