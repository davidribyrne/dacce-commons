package space.dcce.commons.dns.records;

import space.dcce.commons.dns.messages.RecordClass;

public class TxtRecord extends AbstractStringRecord
{

	public TxtRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.TXT, recordClass, timeToLive);
	}
}
