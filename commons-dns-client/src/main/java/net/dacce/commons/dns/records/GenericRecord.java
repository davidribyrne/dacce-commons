package net.dacce.commons.dns.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.mina.core.buffer.IoBuffer;
import net.dacce.commons.dns.messages.RecordClass;

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
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(data).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof GenericRecord))
			return false;
		if (obj == this)
			return true;
		
		GenericRecord o = (GenericRecord) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(data, o.data).isEquals();
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
