package org.apache.directory.server.dns.records;

import org.apache.directory.server.dns.messages.RecordClass;
import org.apache.directory.server.dns.messages.RecordType;

public class PtrRecord extends AbstractHostnameRecord
{
	public PtrRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.PTR, recordClass, timeToLive);
	}
}
