package net.dacce.commons.dns.records;

import net.dacce.commons.dns.messages.RecordClass;

public class PtrRecord extends AbstractHostnameRecord
{
	public PtrRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.PTR, recordClass, timeToLive);
	}
}
