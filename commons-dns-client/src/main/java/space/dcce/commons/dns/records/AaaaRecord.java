package space.dcce.commons.dns.records;

import space.dcce.commons.dns.messages.RecordClass;

// TODO: Auto-generated Javadoc
/**
 * The Class AaaaRecord.
 */
public class AaaaRecord extends AbstractAddressRecord
{


	/**
	 * Instantiates a new aaaa record.
	 *
	 * @param domainName the domain name
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	public AaaaRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.AAAA, recordClass, timeToLive);
	}


	/**
	 * Gets the address length.
	 *
	 * @return the address length
	 */
	@Override
	protected int getAddressLength()
	{
		return 16;
	}

}
