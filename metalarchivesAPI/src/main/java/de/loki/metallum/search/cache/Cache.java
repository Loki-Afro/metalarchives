package de.loki.metallum.search.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import de.loki.metallum.entity.IEntity;
import de.loki.metallum.entity.Track;

public final class Cache implements ICache {
	private final Map<Long, IEntity>	entityCacheMap	= new ConcurrentHashMap<Long, IEntity>();
	private int							maxEntityEntrys	= 5000;
	private final static Cache			instance		= new Cache();

	private Cache() {
	};

	public static Cache getInstance() {
		return instance;
	}

	/**
	 * Clones the entity before putting it into the Cache.
	 */
	@Override
	public final boolean put(final IEntity entity) {
		if (this.entityCacheMap.size() < this.maxEntityEntrys) {
			try {
				final IEntity newObj = (IEntity) entity.clone();
				this.entityCacheMap.put(entity.getId(), newObj);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public final <E extends IEntity> E getEntity(final IEntity entity, final Class<E> clazz) {
		final IEntity entityFromCache = this.entityCacheMap.get(entity.getId());
		if (entityFromCache != null) {
			if (clazz.isInstance(entityFromCache)) {
				try {
					E castedEntityFromCache = clazz.cast(entityFromCache.clone());
					if (clazz == Track.class && !castedEntityFromCache.getName().equals(entity.getName())) {
						return null;
					} else {
						return castedEntityFromCache;
					}
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public final void setMaxEntrys(int max) {
		this.maxEntityEntrys = max;
	}

	@Override
	public final void reset() {
		this.entityCacheMap.clear();
	}

	@Override
	public Iterable<Entry<Long, IEntity>> getAllEntities() {
		return this.entityCacheMap.entrySet();
	}

}
