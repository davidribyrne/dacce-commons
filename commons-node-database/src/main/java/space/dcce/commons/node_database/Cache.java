package space.dcce.commons.node_database;

import java.lang.ref.SoftReference;
import java.util.HashMap;



// TODO: Auto-generated Javadoc
/**
 * The Class Cache.
 *
 * @param <T> the generic type
 */
public abstract class Cache<T>
{


	/** The cache lock. */
	protected final Object cacheLock = new Object();


	/** The operation count. */
	private int operationCount = 0;

	/**
	 * Gets the key.
	 *
	 * @param value the value
	 * @return the key
	 */
	protected abstract Object getKey(T value);


	/** The cache. */
	protected HashMap<Object, SoftReference<T>> cache;


	/**
	 * Instantiates a new cache.
	 */
	protected Cache()
	{
		cache = new HashMap<Object, SoftReference<T>>(1000);
	}

	/**
	 * Contains key.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean containsKey(Object key)
	{
		return cache.containsKey(key);
	}

	/**
	 * Adds the item.
	 *
	 * @param item the item
	 */
	public void addItem(T item)
	{

		synchronized (cacheLock)
		{
			Object key = getKey(item);
			if (cache.containsKey(key))
			{
				SoftReference wr = cache.get(key);
				Object duplicate = wr.get();
				throw new IllegalStateException("Item already exists in cache");
			}
			cache.put(key, new SoftReference<T>(item));
		}
		prune();
	}


	/**
	 * Prune.
	 */
	protected synchronized void prune()
	{
		if (++operationCount >= 1000)
		{
			operationCount = 0;
			synchronized (cacheLock)
			{

				cache.keySet().removeIf(k -> cache.get(k).get() == null);

//				int count = 0;
//				for (Object key : Collections.unmodifiableSet(cache.keySet()))
//				for(Iterator<Object> iterator = cache.keySet().iterator(); iterator.hasNext();)
//				{
//					Object key = iterator.next();
//					if (cache.get(key).get() == null)
//					{
//						iterator.remove(key);
//					}
//				}
//
//				return count;
			}
		}
	}


}
