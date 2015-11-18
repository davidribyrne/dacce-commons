package net.dacce.commons.dns.records;

import net.dacce.commons.dns.messages.RecordClass;

public class CnameRecord extends AbstractHostnameRecord
{

	protected CnameRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.CNAME, recordClass, timeToLive);
	}
}
