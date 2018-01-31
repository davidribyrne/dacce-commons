package space.dcce.commons.dns.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.mina.core.buffer.IoBuffer;

import space.dcce.commons.dns.io.DnsDecodingUtils;
import space.dcce.commons.dns.io.DnsEncodingUtils;
import space.dcce.commons.dns.messages.RecordClass;

public abstract class AbstractStringRecord extends ResourceRecord
{
	private String data;

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(data).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof AbstractStringRecord))
			return false;
		if (obj == this)
			return true;
					
		AbstractStringRecord a = (AbstractStringRecord) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(a.data, data).isEquals();
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).appendSuper(super.toString()).append("data", data).build();
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
