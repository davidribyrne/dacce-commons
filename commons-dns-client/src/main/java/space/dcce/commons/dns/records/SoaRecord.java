package space.dcce.commons.dns.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.mina.core.buffer.IoBuffer;

import space.dcce.commons.dns.io.DnsDecodingUtils;
import space.dcce.commons.dns.io.DnsEncodingUtils;
import space.dcce.commons.dns.messages.RecordClass;

public class SoaRecord extends ResourceRecord
{
	private String mName;
	private String rName;
	private long serial;
	private int refresh;
	private int retry;
	private int expire;
	private long minimum;
	



	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(mName).append(rName)
				.append(serial).append(refresh).append(retry).append(expire)
				.append(minimum).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof SoaRecord))
			return false;
		if (obj == this)
			return true;
		SoaRecord s = (SoaRecord) obj;
		return new EqualsBuilder().append(mName, s.mName).append(rName, s.rName).append(serial, s.rName).
				append(refresh, s.refresh).append(retry, s.retry).append(expire, s.expire).
				append(minimum, s.minimum).appendSuper(super.equals(obj)).isEquals();
	}


	public SoaRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.SOA, recordClass, timeToLive);
	}


	@Override
	public void decodeData(IoBuffer byteBuffer, short length)
	{
		mName = DnsDecodingUtils.getDomainName(byteBuffer);
		rName = DnsDecodingUtils.getDomainName(byteBuffer);
		serial = byteBuffer.getInt();
		refresh = byteBuffer.getInt();
		retry = byteBuffer.getInt();
		expire = byteBuffer.getInt();
		minimum = byteBuffer.getInt();
	}


	@Override
	protected void encodeData(IoBuffer byteBuffer)
	{
		DnsEncodingUtils.putDomainName(byteBuffer, mName);
		DnsEncodingUtils.putDomainName(byteBuffer, rName);
		byteBuffer.putInt((int) serial);
		byteBuffer.putInt(refresh);
		byteBuffer.putInt(retry);
		byteBuffer.putInt(expire);
		byteBuffer.putInt((int) minimum);
		
	}
}
