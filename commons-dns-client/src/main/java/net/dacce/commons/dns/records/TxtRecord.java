package net.dacce.commons.dns.records;

import net.dacce.commons.dns.messages.RecordClass;

public class TxtRecord extends AbstractStringRecord
{

	public TxtRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.TXT, recordClass, timeToLive);
	}
}
