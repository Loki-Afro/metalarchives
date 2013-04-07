package de.loki.metallum.search.cache;

import java.util.Map.Entry;

import de.loki.metallum.entity.IEntity;

/**
 * This class is used to cache previously parsed entities.<br>
 * By the API its just before and after a parsing process begins to avoid unnecessary network access.
 * 
 * @author Zarathustra
 * 
 */
public interface ICache {

	/**
	 * The Default Cache Implementation.<br>
	 * To Change the Cache Implementation, use the "de.loki.metallum.cacheimpl" System-Property
	 */
	public static final String	DEFAULT_CACHE	= CacheImpl.class.getName();

	/**
	 * To get an Object which implements IEntity from the Cache.
	 * 
	 * @param clazz the Class of the IEntity which you want to get from the Cache.
	 * @return the IEntity or null if there is nothing in the Cache which matches the criteria.
	 * @see code de.loki.metallum.entity.IEntity.setId(long id)
	 */
	@Deprecated
	public abstract <E extends IEntity> E getEntity(final IEntity entity, final Class<E> clazz);

	/**
	 * To get an entity Object from the Cache that was previously put into the Cache.<br>
	 * Note that this method clones the entity before you finally get it.
	 * 
	 * @see code de.loki.metallum.entity.IEntity.setId(long id)
	 * @param id the unique id from the entity.
	 * @param clazz the class which you expect to get.
	 * @return the entity from cache if it matches the unique id and class, null if nothing could be found or the id is not greater than 0.
	 */
	public abstract <E extends IEntity> E getEntity(final long id, final Class<E> clazz);

	/**
	 * Resets the cache<br>
	 * If you put an entity in it and call this method then <br>
	 * it won't be accessible anymore by the getEntity method.
	 */
	public abstract void reset();

	public abstract Iterable<Entry<Long, IEntity>> getAllEntities();

	/**
	 * Puts an entity into the cache<br>
	 * if the following conditions are met:
	 * <ul>
	 * <li>The entity is not null</li>
	 * <li>The id of the entity is greater than 0</li>
	 * <li>Maybe in your implementation are more constraints.</li>
	 * </ul>
	 * 
	 * @param entity the entity which should be put into the cache.
	 * @return true if the entity has been added successfully and is accessible by the getEntity Method. False otherwise.
	 */
	public boolean put(IEntity entity);

}