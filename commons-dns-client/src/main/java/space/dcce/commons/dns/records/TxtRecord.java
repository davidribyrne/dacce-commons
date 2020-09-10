package space.dcce.commons.dns.records;

import space.dcce.commons.dns.messages.RecordClass;

// TODO: Auto-generated Javadoc
/**
 * The Class TxtRecord.
 */
public class TxtRecord extends AbstractStringRecord
{

	/**
	 * Instantiates a new txt record.
	 *
	 * @param domainName the domain name
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	public TxtRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.TXT, recordClass, timeToLive);
	}
}
