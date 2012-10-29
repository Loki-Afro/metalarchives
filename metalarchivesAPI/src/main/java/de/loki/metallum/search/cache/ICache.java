package de.loki.metallum.search.cache;

import java.util.Map.Entry;

import de.loki.metallum.entity.IEntity;

public interface ICache {

	// /**
	// * To put a Track into the Cache.
	// *
	// * @param entity the entity to put into the Cache.
	// * @return true if the object is now in the Cache, otherwise false
	// */
	// public abstract void put(Track entity);
	//
	// /**
	// * To put a Track into the Cache.
	// *
	// * @param entity the entity to put into the Cache.
	// * @return true if the object is now in the Cache, otherwise false
	// */
	// public abstract void put(Band entity);
	//
	// /**
	// * To put a Track into the Cache.
	// *
	// * @param entity the entity to put into the Cache.
	// * @return true if the object is now in the Cache, otherwise false
	// */
	// public abstract void put(Disc entity);
	//
	// /**
	// * To put a Member into the Cache.
	// *
	// * @param entity the entity to put into the Cache.
	// * @return true if the object is now in the Cache, otherwise false
	// */
	// public abstract void put(Member entity);
	//
	// /**
	// * To put a Label into the Cache.
	// *
	// * @param entity the entity to put into the Cache.
	// * @return true if the object is now in the Cache, otherwise false
	// */
	// public abstract void put(Label entity);

	/**
	 * To get an Object which implements IEntity from the Cache.
	 * 
	 * @param clazz the Class of the IEntity which you want to get from the Cache.
	 * @return the IEntity or null if there is nothing in the Cache which matches the criteria.
	 * @see code de.loki.metallum.entity.IEntity.setId(long id)
	 */
	public abstract <E extends IEntity> E getEntity(final IEntity entity, final Class<E> clazz);

	public abstract void setMaxEntrys(final int max);

	public abstract void reset();

	public abstract Iterable<Entry<Long, IEntity>> getAllEntities();

	boolean put(IEntity entity);

}