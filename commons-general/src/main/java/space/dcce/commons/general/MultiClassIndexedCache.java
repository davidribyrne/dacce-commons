package space.dcce.commons.general;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiClassIndexedCache
{
	

	private final Map<Class<?>, IndexedCache<Object>> caches;
	private final boolean unique;
	
	public MultiClassIndexedCache(boolean unique)
	{
		this.unique = unique;
		caches = new HashMap<Class<?>, IndexedCache<Object>>();
	}
	
	public Set<Class<?>> getClasses()
	{
		return Collections.unmodifiableSet(caches.keySet());
	}
	private IndexedCache<Object> getCache(Class<Object> clazz)
	{
		if (caches.containsKey(clazz))
			return caches.get(clazz);
		IndexedCache<Object> cache = new IndexedCache<>();
		caches.put(clazz, cache);
		return cache;
	}
	
	public Object getMember(Class clazz, Field field, Object key)
	{
		return getCache(clazz).getMember(field, key);
	}
	
	public List<?> getMembers(Class clazz, Field field, Object key)
	{
		return getCache(clazz).getMembers(field, key);
	}

	public IndexedCache<?> getMembers(Class clazz)
	{
		return getCache(clazz);
	}
	
	
	public boolean contains(Class<Object> clazz, Object value)
	{
		return getCache(clazz).containsValue(value);
	}
	
	public void add(Class clazz, Object object) throws IllegalArgumentException
	{
		IndexedCache<Object> cache = getCache(clazz);
		if (unique && cache.containsValue(object))
			throw new IllegalArgumentException("Object already in class.");
		cache.add(object);
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}

}