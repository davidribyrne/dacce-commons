package org.apache.directory.server.dns.records;

import org.apache.directory.server.dns.messages.RecordClass;
import org.apache.directory.server.dns.messages.RecordType;

public class NsRecord extends AbstractHostnameRecord
{

	protected NsRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.NS, recordClass, timeToLive);
	}

}
