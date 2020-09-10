package space.dcce.commons.dns.records;

import space.dcce.commons.dns.messages.RecordClass;

// TODO: Auto-generated Javadoc
/**
 * The Class NsRecord.
 */
public class NsRecord extends AbstractHostnameRecord
{

	/**
	 * Instantiates a new ns record.
	 *
	 * @param domainName the domain name
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	protected NsRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.NS, recordClass, timeToLive);
	}

}
