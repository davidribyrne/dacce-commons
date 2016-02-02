package net.dacce.commons.dns.records;

import net.dacce.commons.dns.io.DnsDecodingUtils;
import net.dacce.commons.dns.io.DnsEncodingUtils;
import net.dacce.commons.dns.messages.RecordClass;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.mina.core.buffer.IoBuffer;

public class MxRecord extends AbstractHostnameRecord
{
	private int preference;

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(preference).appendSuper(super.hashCode()).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		MxRecord o = (MxRecord) obj;
		return new EqualsBuilder().append(preference, o.preference).appendSuper(super.equals(obj)).isEquals();
	}


	public MxRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.MX, recordClass, timeToLive);
	}


	@Override
	public void decodeData(IoBuffer byteBuffer, short length)
	{
        preference = byteBuffer.getShort() ;
        super.decodeData(byteBuffer, (short) (length - 2));
	}


	@Override
	public void encodeData(IoBuffer byteBuffer)
	{
        byteBuffer.putShort( (short) preference );
        super.encodeData(byteBuffer);
	}
}
