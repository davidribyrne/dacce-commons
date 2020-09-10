package space.dcce.commons.dns.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import space.dcce.commons.dns.client.cache.DnsCache;
import space.dcce.commons.dns.messages.QuestionRecord;
import space.dcce.commons.dns.records.ResourceRecord;

// TODO: Auto-generated Javadoc
/**
 * The Class DnsTransaction.
 */
public class DnsTransaction
{

	/** The question. */
	private QuestionRecord question;
	
	/** The answers. */
	private final List<ResourceRecord> answers;
	
	/** The negative response count. */
	private int negativeResponseCount;
	
	/** The dns cache. */
	private DnsCache dnsCache;
	
	/** The recurse. */
	private boolean recurse;

	/**
	 * Instantiates a new dns transaction.
	 *
	 * @param question the question
	 * @param recurse the recurse
	 */
	public DnsTransaction(QuestionRecord question, boolean recurse)
	{
		this.question = question;
		this.recurse = recurse;
		answers = new ArrayList<ResourceRecord>(1);
	}
	

	/**
	 * Adds the answers.
	 *
	 * @param newAnswers the new answers
	 */
	public void addAnswers(List<ResourceRecord> newAnswers)
	{
		if (newAnswers == null)
			return;

		if (dnsCache != null)
		{
			dnsCache.add(question, newAnswers);
		}
		
		if (newAnswers.isEmpty())
		{
			negativeResponseCount++;
			return;
		}
		
		for(ResourceRecord response: newAnswers)
		{
			if (response != null)
				this.answers.add(response);
		}
	}

	/**
	 * Gets the question.
	 *
	 * @return the question
	 */
	public QuestionRecord getQuestion()
	{
		return question;
	}

	/**
	 * Checks for answer.
	 *
	 * @return true, if successful
	 */
	public boolean hasAnswer()
	{
		return (answers != null && !answers.isEmpty());
	}
	
	/**
	 * Gets the answers.
	 *
	 * @return the answers
	 */
	public List<ResourceRecord> getAnswers()
	{
		return answers;
	}

	/**
	 * Gets the negative response count.
	 *
	 * @return the negative response count
	 */
	public int getNegativeResponseCount()
	{
		return negativeResponseCount;
	}

	/**
	 * Gets the cache.
	 *
	 * @return the cache
	 */
	public DnsCache getCache()
	{
		return dnsCache;
	}

	/**
	 * Sets the cache.
	 *
	 * @param cache the new cache
	 */
	public void setCache(DnsCache cache)
	{
		this.dnsCache = cache;
	}


	/**
	 * Checks if is recurse.
	 *
	 * @return true, if is recurse
	 */
	public boolean isRecurse()
	{
		return recurse;
	}


	/**
	 * Sets the recurse.
	 *
	 * @param recurse the new recurse
	 */
	public void setRecurse(boolean recurse)
	{
		this.recurse = recurse;
	}


	/**
	 * Adds the negative response.
	 */
	public void addNegativeResponse()
	{
		negativeResponseCount++;
	}


	/**
	 * Checks if is negative response.
	 *
	 * @return true, if is negative response
	 */
	public boolean isNegativeResponse()
	{
		return !hasAnswer() && negativeResponseCount > 0;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		ToStringBuilder tsb = new ToStringBuilder(this).append("question", question);
		for (ResourceRecord answer: answers)
		{
			tsb.append("answer", answer);
		}
		tsb.append("negativeResponseCount", negativeResponseCount);
		tsb.append("recurse", recurse);
		return tsb.build();
	}

}
