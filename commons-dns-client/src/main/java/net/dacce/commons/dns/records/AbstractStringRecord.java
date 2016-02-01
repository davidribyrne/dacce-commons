package net.dacce.commons.dns.records;

import net.dacce.commons.dns.io.DnsDecodingUtils;
import net.dacce.commons.dns.io.DnsEncodingUtils;
import net.dacce.commons.dns.messages.RecordClass;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.mina.core.buffer.IoBuffer;

public abstract class AbstractStringRecord extends ResourceRecord
{
	private String data;

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getRecordType()).append(getRecordClass()).append(data).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		AbstractStringRecord a = (AbstractStringRecord) obj;
		return new EqualsBuilder().append(a.data, data).isEquals();
	}
	
	@Override
	public String toString()
	{
//		StringBuilder = new StringBuilder();
		return data;
	}


	public AbstractStringRecord(String domainName, RecordType recordType, RecordClass recordClass, int timeToLive)
	{
		super(domainName, recordType, recordClass, timeToLive);
	}


	@Override
	public void decodeData(IoBuffer byteBuffer, short length)
	{
		data = DnsDecodingUtils.getCharacterString(byteBuffer, length);
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
