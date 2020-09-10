package space.dcce.commons.node_database;

import java.lang.ref.SoftReference;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class UniqueDatumCache.
 *
 * @param <D> the generic type
 */
public class UniqueDatumCache<D extends UniqueDatum> extends Cache<D>
{


	/**
	 * Instantiates a new unique datum cache.
	 */
	public UniqueDatumCache()
	{
	}


	/**
	 * Gets the key.
	 *
	 * @param value the value
	 * @return the key
	 */
	@Override
	protected Object getKey(D value)
	{
		return value.getID();
	}
	
	/**
	 * Gets the value.
	 *
	 * @param id the id
	 * @return the value
	 */
	public D getValue(UUID id)
	{
		prune();
		synchronized (cacheLock)
		{
			SoftReference<D> wr = cache.get(id);
			if (wr == null)
				return null;
			return cache.get(id).get();
		}
	}
}
