package space.dcce.commons.dns.records;

import space.dcce.commons.dns.messages.RecordClass;

// TODO: Auto-generated Javadoc
/**
 * The Class PtrRecord.
 */
public class PtrRecord extends AbstractHostnameRecord
{
	
	/**
	 * Instantiates a new ptr record.
	 *
	 * @param domainName the domain name
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	public PtrRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.PTR, recordClass, timeToLive);
	}
}
