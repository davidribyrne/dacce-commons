package org.apache.directory.server.dns.records;

import org.apache.directory.server.dns.messages.RecordClass;
import org.apache.directory.server.dns.messages.RecordType;

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
