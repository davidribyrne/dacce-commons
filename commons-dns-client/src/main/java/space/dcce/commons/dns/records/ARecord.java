package space.dcce.commons.dns.records;

import space.dcce.commons.dns.messages.RecordClass;

// TODO: Auto-generated Javadoc
/**
 * The Class ARecord.
 */
public class ARecord extends AbstractAddressRecord
{


	/**
	 * Instantiates a new a record.
	 *
	 * @param domainName the domain name
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	public ARecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.A, recordClass, timeToLive);
	}


	/**
	 * Gets the address length.
	 *
	 * @return the address length
	 */
	@Override
	protected int getAddressLength()
	{
		return 4;
	}
}
