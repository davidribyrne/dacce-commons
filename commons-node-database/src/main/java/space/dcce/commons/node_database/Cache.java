package space.dcce.commons.node_database;

import java.lang.ref.SoftReference;
import java.util.HashMap;



public abstract class Cache<T>
{


	protected final Object cacheLock = new Object();


	private int operationCount = 0;

	protected abstract Object getKey(T value);


	protected HashMap<Object, SoftReference<T>> cache;


	protected Cache()
	{
		cache = new HashMap<Object, SoftReference<T>>(1000);
	}

	public boolean containsKey(Object key)
	{
		return cache.containsKey(key);
	}

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
