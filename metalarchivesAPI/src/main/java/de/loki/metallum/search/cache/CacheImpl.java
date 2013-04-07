package de.loki.metallum.search.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import de.loki.metallum.entity.IEntity;

public final class CacheImpl implements ICache {
	private final Map<Long, IEntity>	entityCacheMap	= new ConcurrentHashMap<Long, IEntity>();
	private final int					maxEntityEntrys	= 5000;
	private static Logger				logger			= Logger.getLogger(CacheImpl.class);

	/**
	 * Clones the entity before putting it into the Cache.
	 */
	@Override
	public final boolean put(final IEntity entity) {
		if (entity == null) {
			logger.warn("Unable to put an entitiy into the cache, because it was null!");
			return false;
		}
		long entityId = entity.getId();
		if (entityId <= 0) {
			logger.warn("Unable to put an entity: " + entity + " [" + entity.getClass() + "] into the cache, because ID was " + entityId);
			return false;
		}
		if (this.entityCacheMap.size() < this.maxEntityEntrys) {
			try {
				final IEntity newObj = (IEntity) entity.clone();
				this.entityCacheMap.put(entityId, newObj);
				logger.info("new Cache entry: " + newObj + ", cache size is: " + this.entityCacheMap.size());
			} catch (Throwable e) {
				logger.error("Put entity into the cache failed: " + entity + ", size fo the Cache is now: " + this.entityCacheMap.size());
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public final <E extends IEntity> E getEntity(final IEntity entity, final Class<E> clazz) {
		if (entity != null) {
			return getEntity(entity.getId(), clazz);
		} else {
			return null;
		}
	}

	@Override
	public final void reset() {
		this.entityCacheMap.clear();
	}

	@Override
	public Iterable<Entry<Long, IEntity>> getAllEntities() {
		return this.entityCacheMap.entrySet();
	}

	@Override
	public <E extends IEntity> E getEntity(final long id, final Class<E> clazz) {
		if (id <= 0) {
			logger.warn("Unable tog et Entity from Cache, id was " + id);
			return null;
		}
		final IEntity entityFromCache = this.entityCacheMap.get(id);
		if (entityFromCache != null) {
			if (clazz.isInstance(entityFromCache)) {
				try {
					E castedEntityFromCache = clazz.cast(entityFromCache.clone());
					return castedEntityFromCache;
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
