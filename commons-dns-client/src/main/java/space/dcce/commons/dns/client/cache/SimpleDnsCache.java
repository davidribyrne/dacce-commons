package space.dcce.commons.dns.client.cache;


import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import space.dcce.commons.dns.messages.QuestionRecord;
import space.dcce.commons.dns.records.ResourceRecord;
// TODO: Auto-generated Javadoc

/**
 * The Class SimpleDnsCache.
 */
public class SimpleDnsCache implements DnsCache 
{
	
	/** The any cache. */
	private final RecordCache<String> anyCache;
	
	/** The question cache. */
	private final RecordCache<QuestionRecord> questionCache;
//	private final List<QuestionRecord> negativeCache;
//	private final boolean cacheNegativeResponses;
	
	/**
 * Instantiates a new simple dns cache.
 */
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


	/**
	 * Gets the.
	 *
	 * @param question the question
	 * @return the list
	 */
	/* (non-Javadoc)
	 * @see space.dcce.commons.dns.client.cache.DnsCache#get(space.dcce.commons.dns.messages.QuestionRecord)
	 */
	@Override
	public List<ResourceRecord> get(QuestionRecord question)
	{
//		if (cacheNegativeResponses && negativeCache.contains(question))
//		{
//			return Collections.emptyList();
//		}
		return questionCache.getRecords(question);
	}
	
	/**
	 * Adds the.
	 *
	 * @param question the question
	 * @param records the records
	 */
	/* (non-Javadoc)
	 * @see space.dcce.commons.dns.client.cache.DnsCache#add(space.dcce.commons.dns.messages.QuestionRecord, java.util.List)
	 */
	@Override
	public void add(QuestionRecord question, List<ResourceRecord> records)
	{
//		negativeCache.remove(question);
		questionCache.addRecords(question, records);
		anyCache.addRecords(question.getDomainName(), records);
	}

	/**
	 * Contains.
	 *
	 * @param question the question
	 * @return true, if successful
	 */
	/* (non-Javadoc)
	 * @see space.dcce.commons.dns.client.cache.DnsCache#contains(space.dcce.commons.dns.messages.QuestionRecord)
	 */
	@Override
	public boolean contains(QuestionRecord question)
	{
		return questionCache.containsKey(question); // || negativeCache.contains(question);
	}
	
	/**
	 * Gets the any.
	 *
	 * @param domainName the domain name
	 * @return the any
	 */
	/* (non-Javadoc)
	 * @see space.dcce.commons.dns.client.cache.DnsCache#getAny(java.lang.String)
	 */
	@Override
	public List<ResourceRecord> getAny(String domainName)
	{
		return anyCache.getRecords(domainName);
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
			.append("anyCache", anyCache)
			.append("questionCache")
			.toString();
	}


	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
			.append(anyCache.hashCode())
			.append(questionCache.hashCode())
			.toHashCode();
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
		if (!(obj instanceof SimpleDnsCache))
			return false;
		
		SimpleDnsCache o = (SimpleDnsCache) obj;
		return new EqualsBuilder()
			.append(anyCache, o.anyCache)
			.append(o.questionCache, questionCache)
			.isEquals();
	}

}
