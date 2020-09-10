package space.dcce.commons.dns.records;

import space.dcce.commons.dns.messages.RecordClass;

// TODO: Auto-generated Javadoc
/**
 * The Class CnameRecord.
 */
public class CnameRecord extends AbstractHostnameRecord
{

	/**
	 * Instantiates a new cname record.
	 *
	 * @param domainName the domain name
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	protected CnameRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.CNAME, recordClass, timeToLive);
	}
}
