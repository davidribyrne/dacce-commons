package net.dacce.commons.dns.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.mina.core.buffer.IoBuffer;
import net.dacce.commons.dns.io.DnsDecodingUtils;
import net.dacce.commons.dns.io.DnsEncodingUtils;
import net.dacce.commons.dns.messages.RecordClass;

public abstract class AbstractHostnameRecord extends ResourceRecord
{

	private String value;
	
	protected AbstractHostnameRecord(String domainName, RecordType recordType, RecordClass recordClass, int timeToLive)
	{
		super(domainName, recordType, recordClass, timeToLive);
	}


	@Override
	public String toString()
	{
		return new ToStringBuilder(this).appendSuper(super.toString()).append("value", value).build();
	}


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(value).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof AbstractHostnameRecord))
			return false;
		if (obj == this)
			return true;
		
		AbstractHostnameRecord c = (AbstractHostnameRecord) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(value, c.value).isEquals();
	}



	@Override
	public void decodeData(IoBuffer byteBuffer, short length)
	{
		value = DnsDecodingUtils.getDomainName(byteBuffer);
	}


	@Override
	protected void encodeData(IoBuffer byteBuffer)
	{
        DnsEncodingUtils.putDomainName( byteBuffer, value );
	}


	public String getValue()
	{
		return value;
	}


	public void setValue(String value)
	{
		this.value = value;
	}

}
