package org.apache.directory.server.dns.records;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.directory.server.dns.io.DnsDecodingUtils;
import org.apache.directory.server.dns.io.DnsEncodingUtils;
import org.apache.directory.server.dns.messages.RecordClass;
import org.apache.directory.server.dns.messages.RecordType;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class AbstractStringRecord extends ResourceRecord
{
	private final static Logger logger = LoggerFactory.getLogger(AbstractStringRecord.class);

	private String data;

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(data).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		AbstractStringRecord a = (AbstractStringRecord) obj;
		return new EqualsBuilder().append(a.data, data).isEquals();
	}


	public AbstractStringRecord(String domainName, RecordType recordType, RecordClass recordClass, int timeToLive)
	{
		super(domainName, recordType, recordClass, timeToLive);
	}


	@Override
	public void decodeData(IoBuffer byteBuffer, short length)
	{
		data = DnsDecodingUtils.getCharacterString(byteBuffer);
	}


	@Override
	protected void encodeData(IoBuffer byteBuffer)
	{
		DnsEncodingUtils.putCharacterString(byteBuffer, data);
	}


	public String getData()
	{
		return data;
	}


	public void setData(String data)
	{
		this.data = data;
	}
}
