package net.dacce.commons.dns.client.cache;


import net.dacce.commons.dns.client.DnsTransaction;
import net.dacce.commons.general.UniqueList;

import org.apache.directory.server.dns.messages.QuestionRecord;
import org.apache.directory.server.dns.messages.RecordType;
import org.apache.directory.server.dns.records.ResourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
public class SimpleDnsCache 
{
	private final static Logger logger = LoggerFactory.getLogger(SimpleDnsCache.class);

	private final RecordCache<String> anyCache;
	private final RecordCache<QuestionRecord> questionCache;
//	private final List<QuestionRecord> negativeCache;
//	private final boolean cacheNegativeResponses;
	
	public SimpleDnsCache()
	{
//		this.cacheNegativeResponses = cacheNegativeResponses;
//		if (cacheNegativeResponses)
//			negativeCache = new UniqueList<QuestionRecord>();
//		else
//			negativeCache = null;
		anyCache = new RecordCache<String>();
		questionCache = new RecordCache<QuestionRecord>();
	}


	public List<ResourceRecord> get(QuestionRecord question)
	{
//		if (cacheNegativeResponses && negativeCache.contains(question))
//		{
//			return Collections.emptyList();
//		}
		return questionCache.getRecords(question);
	}
	
	public void add(QuestionRecord question, List<ResourceRecord> records)
	{
//		negativeCache.remove(question);
		questionCache.addRecords(question, records);
		anyCache.addRecords(question.getDomainName(), records);
	}

	public boolean contains(QuestionRecord question)
	{
		return questionCache.containsKey(question); // || negativeCache.contains(question);
	}
	
	public List<ResourceRecord> getAny(String domainName)
	{
		return anyCache.getRecords(domainName);
	}
}
