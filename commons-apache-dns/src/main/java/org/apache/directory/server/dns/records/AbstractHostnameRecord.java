package org.apache.directory.server.dns.records;

import org.apache.directory.server.dns.io.DnsDecodingUtils;
import org.apache.directory.server.dns.io.DnsEncodingUtils;
import org.apache.directory.server.dns.messages.RecordClass;
import org.apache.directory.server.dns.messages.RecordType;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHostnameRecord extends ResourceRecord
{

	private final static Logger logger = LoggerFactory.getLogger(AbstractHostnameRecord.class);

	private String name;
	
	protected AbstractHostnameRecord(String domainName, RecordType recordType, RecordClass recordClass, int timeToLive)
	{
		super(domainName, recordType, recordClass, timeToLive);
	}


	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}


	@Override
	public int hashCode()
	{
		return name.hashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		AbstractHostnameRecord c = (AbstractHostnameRecord) obj;
		return name.equals(c.name);
	}



	@Override
	public void decodeData(IoBuffer byteBuffer, short length)
	{
		name = DnsDecodingUtils.getDomainName(byteBuffer);
	}


	@Override
	protected void encodeData(IoBuffer byteBuffer)
	{
        DnsEncodingUtils.putDomainName( byteBuffer, name );
	}

}
