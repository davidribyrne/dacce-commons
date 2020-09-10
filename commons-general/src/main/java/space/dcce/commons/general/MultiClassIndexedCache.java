package space.dcce.commons.general;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiClassIndexedCache.
 */
public class MultiClassIndexedCache
{
	

	/** The caches. */
	private final Map<Class<?>, IndexedCache<Object>> caches;
	
	/** The unique. */
	private final boolean unique;
	
	/**
	 * Instantiates a new multi class indexed cache.
	 *
	 * @param unique the unique
	 */
	public MultiClassIndexedCache(boolean unique)
	{
		this.unique = unique;
		caches = new HashMap<Class<?>, IndexedCache<Object>>();
	}
	
	/**
	 * Gets the classes.
	 *
	 * @return the classes
	 */
	public Set<Class<?>> getClasses()
	{
		return Collections.unmodifiableSet(caches.keySet());
	}
	
	/**
	 * Gets the cache.
	 *
	 * @param clazz the clazz
	 * @return the cache
	 */
	private IndexedCache<Object> getCache(Class<Object> clazz)
	{
		if (caches.containsKey(clazz))
			return caches.get(clazz);
		IndexedCache<Object> cache = new IndexedCache<>();
		caches.put(clazz, cache);
		return cache;
	}
	
	/**
	 * Gets the member.
	 *
	 * @param clazz the clazz
	 * @param field the field
	 * @param key the key
	 * @return the member
	 */
	public Object getMember(Class clazz, Field field, Object key)
	{
		return getCache(clazz).getMember(field, key);
	}
	
	/**
	 * Gets the members.
	 *
	 * @param clazz the clazz
	 * @param field the field
	 * @param key the key
	 * @return the members
	 */
	public List<?> getMembers(Class clazz, Field field, Object key)
	{
		return getCache(clazz).getMembers(field, key);
	}

	/**
	 * Gets the members.
	 *
	 * @param clazz the clazz
	 * @return the members
	 */
	public IndexedCache<?> getMembers(Class clazz)
	{
		return getCache(clazz);
	}
	
	
	/**
	 * Contains.
	 *
	 * @param clazz the clazz
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean contains(Class<Object> clazz, Object value)
	{
		return getCache(clazz).containsValue(value);
	}
	
	/**
	 * Adds the.
	 *
	 * @param clazz the clazz
	 * @param object the object
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void add(Class clazz, Object object) throws IllegalArgumentException
	{
		IndexedCache<Object> cache = getCache(clazz);
		if (unique && cache.containsValue(object))
			throw new IllegalArgumentException("Object already in class.");
		cache.add(object);
	}
	
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}

}
