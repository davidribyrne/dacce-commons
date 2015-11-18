package net.dacce.commons.dns.records;

import net.dacce.commons.dns.io.DnsDecodingUtils;
import net.dacce.commons.dns.io.DnsEncodingUtils;
import net.dacce.commons.dns.messages.RecordClass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MxRecord extends ResourceRecord
{
	private final static Logger logger = LoggerFactory.getLogger(MxRecord.class);

	private int preference;
	private String server;



	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(preference).append(server).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		MxRecord o = (MxRecord) obj;
		return new EqualsBuilder().append(preference, o.preference).append(server, o.server).isEquals();
	}


	public MxRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.MX, recordClass, timeToLive);
	}


	@Override
	public void decodeData(IoBuffer byteBuffer, short length)
	{
		
        preference = byteBuffer.getShort() ;
        server = DnsDecodingUtils.getDomainName( byteBuffer );

	}


	@Override
	public void encodeData(IoBuffer byteBuffer)
	{
        byteBuffer.putShort( (short) preference );
        DnsEncodingUtils.putDomainName( byteBuffer, server );
	}
}
