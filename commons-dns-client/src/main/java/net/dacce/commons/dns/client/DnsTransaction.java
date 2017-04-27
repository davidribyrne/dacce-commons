package net.dacce.commons.dns.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import net.dacce.commons.dns.client.cache.DnsCache;
import net.dacce.commons.dns.messages.QuestionRecord;
import net.dacce.commons.dns.records.ResourceRecord;

public class DnsTransaction
{

	private QuestionRecord question;
	private final List<ResourceRecord> answers;
	private int negativeResponseCount;
	private DnsCache dnsCache;
	private boolean recurse;

	public DnsTransaction(QuestionRecord question, boolean recurse)
	{
		this.question = question;
		this.recurse = recurse;
		answers = new ArrayList<ResourceRecord>(1);
	}
	

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

	public QuestionRecord getQuestion()
	{
		return question;
	}

	public boolean hasAnswer()
	{
		return (answers != null && !answers.isEmpty());
	}
	
	public List<ResourceRecord> getAnswers()
	{
		return answers;
	}

	public int getNegativeResponseCount()
	{
		return negativeResponseCount;
	}

	public DnsCache getCache()
	{
		return dnsCache;
	}

	public void setCache(DnsCache cache)
	{
		this.dnsCache = cache;
	}


	public boolean isRecurse()
	{
		return recurse;
	}


	public void setRecurse(boolean recurse)
	{
		this.recurse = recurse;
	}


	public void addNegativeResponse()
	{
		negativeResponseCount++;
	}


	public boolean isNegativeResponse()
	{
		return !hasAnswer() && negativeResponseCount > 0;
	}
	
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
