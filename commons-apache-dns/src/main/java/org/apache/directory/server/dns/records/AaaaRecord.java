package org.apache.directory.server.dns.records;

import org.apache.directory.server.dns.messages.RecordClass;
import org.apache.directory.server.dns.messages.RecordType;

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
