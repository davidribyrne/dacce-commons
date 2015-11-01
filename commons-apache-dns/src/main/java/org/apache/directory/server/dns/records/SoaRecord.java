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

public class SoaRecord extends ResourceRecord
{
	private final static Logger logger = LoggerFactory.getLogger(SoaRecord.class);

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
		return new HashCodeBuilder().append(mName).append(rName).append(serial).append(refresh).
				append(retry).append(expire).append(minimum).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		SoaRecord s = (SoaRecord) obj;
		return new EqualsBuilder().append(mName, s.mName).append(rName, s.rName).append(serial, s.rName).
				append(refresh, s.refresh).append(retry, s.retry).append(expire, s.expire).
				append(minimum, s.minimum).isEquals();
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
