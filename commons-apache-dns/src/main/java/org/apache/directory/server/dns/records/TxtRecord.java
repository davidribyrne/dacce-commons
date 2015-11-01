package org.apache.directory.server.dns.records;

import org.apache.directory.server.dns.messages.RecordClass;
import org.apache.directory.server.dns.messages.RecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TxtRecord extends AbstractStringRecord
{

	public TxtRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.TXT, recordClass, timeToLive);
	}
}
