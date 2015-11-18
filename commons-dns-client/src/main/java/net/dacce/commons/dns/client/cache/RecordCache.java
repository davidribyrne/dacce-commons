package net.dacce.commons.dns.client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.dacce.commons.dns.records.ResourceRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordCache <K> extends HashMap<K, List<ResourceRecord>>
{
	private final static Logger logger = LoggerFactory.getLogger(RecordCache.class);

	public RecordCache()
	{
	}
	
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

}
