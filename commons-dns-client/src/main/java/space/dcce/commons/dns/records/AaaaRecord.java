package space.dcce.commons.dns.records;

import space.dcce.commons.dns.messages.RecordClass;

public class AaaaRecord extends AbstractAddressRecord
{


	public AaaaRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.AAAA, recordClass, timeToLive);
	}


	@Override
	protected int getAddressLength()
	{
		return 16;
	}

}
