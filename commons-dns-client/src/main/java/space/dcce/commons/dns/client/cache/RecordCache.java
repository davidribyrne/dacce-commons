package space.dcce.commons.dns.client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import space.dcce.commons.dns.records.ResourceRecord;

// TODO: Auto-generated Javadoc
/**
 * The Class RecordCache.
 *
 * @param <K> the key type
 */
public class RecordCache <K> extends HashMap<K, List<ResourceRecord>>
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new record cache.
	 */
	public RecordCache()
	{
	}
	
	/**
	 * Gets the records.
	 *
	 * @param key the key
	 * @return the records
	 */
	public List<ResourceRecord> getRecords(K key)
	{
		if (containsKey(key))
		{
			return get(key);
		}
		List<ResourceRecord> r = new ArrayList<ResourceRecord>();
		put(key, r);
		return r;
	}
	
	/**
	 * Adds the records.
	 *
	 * @param key the key
	 * @param records the records
	 */
	public void addRecords(K key, List<ResourceRecord> records)
	{
		if (!containsKey(key))
		{
			put(key, new ArrayList<ResourceRecord>());
		}
		if (records != null)
		{
			get(key).addAll(records);
		}
	}


	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.append(this)
				.toString();
	}

}
