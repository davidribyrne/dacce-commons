package net.dacce.commons.dns.client.cache;


import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import net.dacce.commons.dns.messages.QuestionRecord;
import net.dacce.commons.dns.records.ResourceRecord;
public class SimpleDnsCache implements DnsCache 
{
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


	/* (non-Javadoc)
	 * @see net.dacce.commons.dns.client.cache.DnsCache#get(net.dacce.commons.dns.messages.QuestionRecord)
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
	
	/* (non-Javadoc)
	 * @see net.dacce.commons.dns.client.cache.DnsCache#add(net.dacce.commons.dns.messages.QuestionRecord, java.util.List)
	 */
	@Override
	public void add(QuestionRecord question, List<ResourceRecord> records)
	{
//		negativeCache.remove(question);
		questionCache.addRecords(question, records);
		anyCache.addRecords(question.getDomainName(), records);
	}

	/* (non-Javadoc)
	 * @see net.dacce.commons.dns.client.cache.DnsCache#contains(net.dacce.commons.dns.messages.QuestionRecord)
	 */
	@Override
	public boolean contains(QuestionRecord question)
	{
		return questionCache.containsKey(question); // || negativeCache.contains(question);
	}
	
	/* (non-Javadoc)
	 * @see net.dacce.commons.dns.client.cache.DnsCache#getAny(java.lang.String)
	 */
	@Override
	public List<ResourceRecord> getAny(String domainName)
	{
		return anyCache.getRecords(domainName);
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
			.append("anyCache", anyCache)
			.append("questionCache")
			.toString();
	}


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
			.append(anyCache.hashCode())
			.append(questionCache.hashCode())
			.toHashCode();
	}


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
