package space.dcce.commons.dns.records;

import space.dcce.commons.dns.messages.RecordClass;

public class PtrRecord extends AbstractHostnameRecord
{
	public PtrRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.PTR, recordClass, timeToLive);
	}
}
