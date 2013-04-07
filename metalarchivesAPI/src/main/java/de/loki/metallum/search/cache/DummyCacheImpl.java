package de.loki.metallum.search.cache;

import java.util.Map.Entry;

import de.loki.metallum.entity.IEntity;

public class DummyCacheImpl implements ICache {

	@Override
	public <E extends IEntity> E getEntity(final IEntity entity, final Class<E> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterable<Entry<Long, IEntity>> getAllEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean put(final IEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <E extends IEntity> E getEntity(final long id, final Class<E> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
