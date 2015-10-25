package net.dacce.commons.dns;

import org.apache.directory.server.dns.messages.QuestionRecord;
import org.apache.directory.server.dns.messages.ResourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DnsTransaction
{

	private QuestionRecord question;
	private List<ResourceRecord> responses;

	public DnsTransaction(QuestionRecord question)
	{
		this.question = question;
	}
	
	public void addResponse(ResourceRecord response)
	{
		if (responses == null)
		{
			responses = new ArrayList<ResourceRecord>(1);
		}
		responses.add(response);
	}

	public QuestionRecord getQuestion()
	{
		return question;
	}

	public List<ResourceRecord> getResponses()
	{
		return responses;
	}
}
