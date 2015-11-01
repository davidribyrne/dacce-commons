package org.apache.directory.server.dns.records;

import org.apache.directory.server.dns.messages.RecordClass;
import org.apache.directory.server.dns.messages.RecordType;

public class CnameRecord extends AbstractHostnameRecord
{

	protected CnameRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.CNAME, recordClass, timeToLive);
	}
}
