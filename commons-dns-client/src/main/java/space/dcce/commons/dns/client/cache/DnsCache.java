package space.dcce.commons.dns.client.cache;

import java.util.List;

import space.dcce.commons.dns.messages.QuestionRecord;
import space.dcce.commons.dns.records.ResourceRecord;


public interface DnsCache
{

	public List<ResourceRecord> get(QuestionRecord question);


	public void add(QuestionRecord question, List<ResourceRecord> records);


	public boolean contains(QuestionRecord question);


	public List<ResourceRecord> getAny(String domainName);

}
