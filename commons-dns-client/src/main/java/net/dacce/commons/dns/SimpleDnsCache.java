package net.dacce.commons.dns;


import org.apache.directory.server.dns.messages.QuestionRecord;
import org.apache.directory.server.dns.messages.ResourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
public class SimpleDnsCache 
{
	private final static Logger logger = LoggerFactory.getLogger(SimpleDnsCache.class);

	private final Map<DnsCacheKey, List<ResourceRecord>> cache;
	public SimpleDnsCache()
	{
		cache = new HashMap<DnsCacheKey, List<ResourceRecord>>();
	}


	public List<ResourceRecord> get(QuestionRecord question)
	{
		return cache.get(new DnsCacheKey(question));
	}
	
	public void add(DnsTransaction transaction)
	{
		cache.put(new DnsCacheKey(transaction.getQuestion()), transaction.getResponses());
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(DnsCacheKey key: cache.keySet())
		{
			sb.append(key.toString());
			sb.append(": ");
			cache.get(key).toString();
			sb.append("\n");
		}
		return sb.toString();
	}



}
