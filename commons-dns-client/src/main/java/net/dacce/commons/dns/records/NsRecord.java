package net.dacce.commons.dns.records;

import net.dacce.commons.dns.messages.RecordClass;

public class NsRecord extends AbstractHostnameRecord
{

	protected NsRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.NS, recordClass, timeToLive);
	}

}
