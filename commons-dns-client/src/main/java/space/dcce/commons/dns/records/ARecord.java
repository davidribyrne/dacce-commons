package space.dcce.commons.dns.records;

import space.dcce.commons.dns.messages.RecordClass;

public class ARecord extends AbstractAddressRecord
{


	public ARecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.A, recordClass, timeToLive);
	}


	@Override
	protected int getAddressLength()
	{
		return 4;
	}
}
