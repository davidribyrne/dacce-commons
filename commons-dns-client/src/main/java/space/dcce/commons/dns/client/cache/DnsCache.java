package space.dcce.commons.dns.client.cache;

import java.util.List;

import space.dcce.commons.dns.messages.QuestionRecord;
import space.dcce.commons.dns.records.ResourceRecord;


// TODO: Auto-generated Javadoc
/**
 * The Interface DnsCache.
 */
public interface DnsCache
{

	/**
	 * Gets the.
	 *
	 * @param question the question
	 * @return the list
	 */
	public List<ResourceRecord> get(QuestionRecord question);


	/**
	 * Adds the.
	 *
	 * @param question the question
	 * @param records the records
	 */
	public void add(QuestionRecord question, List<ResourceRecord> records);


	/**
	 * Contains.
	 *
	 * @param question the question
	 * @return true, if successful
	 */
	public boolean contains(QuestionRecord question);


	/**
	 * Gets the any.
	 *
	 * @param domainName the domain name
	 * @return the any
	 */
	public List<ResourceRecord> getAny(String domainName);

}
