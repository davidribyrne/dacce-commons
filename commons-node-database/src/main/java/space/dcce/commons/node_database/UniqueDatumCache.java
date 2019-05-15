package space.dcce.commons.node_database;

import java.lang.ref.SoftReference;
import java.util.UUID;

public class UniqueDatumCache<D extends UniqueDatum> extends Cache<D>
{


	public UniqueDatumCache()
	{
	}


	@Override
	protected Object getKey(D value)
	{
		return value.getID();
	}
	
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
