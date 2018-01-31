package space.dcce.commons.dns.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.mina.core.buffer.IoBuffer;

import space.dcce.commons.dns.messages.RecordClass;

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
		if (!(obj instanceof MxRecord))
			return false;
		if (obj == this)
			return true;
		
		MxRecord o = (MxRecord) obj;
		return new EqualsBuilder().append(preference, o.preference).appendSuper(super.equals(obj)).isEquals();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).appendSuper(super.toString()).append("preference", preference).build();
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
