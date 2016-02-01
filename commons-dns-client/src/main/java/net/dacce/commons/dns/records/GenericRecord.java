package net.dacce.commons.dns.records;

import net.dacce.commons.dns.messages.RecordClass;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.mina.core.buffer.IoBuffer;

public class GenericRecord extends ResourceRecord
{
	private byte[] data;
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(data).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		GenericRecord o = (GenericRecord) obj;
		return new EqualsBuilder().append(data, o.data).isEquals();
	}


	public GenericRecord(String domainName, RecordType recordType, RecordClass recordClass, int timeToLive)
	{
		super(domainName, recordType, recordClass, timeToLive);
	}


	@Override
	public void decodeData(IoBuffer byteBuffer, short length)
	{
		data = new byte[length];
		byteBuffer.get(data);
	}


	@Override
	protected void encodeData(IoBuffer byteBuffer)
	{
		byteBuffer.put(data);
	}


	public byte[] getData()
	{
		return data;
	}


	public void setData(byte[] data)
	{
		this.data = data;
	}
}
