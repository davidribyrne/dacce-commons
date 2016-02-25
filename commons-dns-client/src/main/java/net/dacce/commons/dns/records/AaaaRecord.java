package net.dacce.commons.dns.records;

import net.dacce.commons.dns.messages.RecordClass;

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
