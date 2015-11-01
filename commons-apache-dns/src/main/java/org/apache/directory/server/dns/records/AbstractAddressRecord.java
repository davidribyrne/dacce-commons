package org.apache.directory.server.dns.records;

import net.dacce.commons.netaddr.InvalidIPAddressFormatException;
import net.dacce.commons.netaddr.SimpleInetAddress;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.directory.server.dns.messages.RecordClass;
import org.apache.directory.server.dns.messages.RecordType;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAddressRecord extends ResourceRecord
{
	private final static Logger logger = LoggerFactory.getLogger(AbstractAddressRecord.class);

	private SimpleInetAddress address;
	
	protected AbstractAddressRecord(String domainName, RecordType recordType, RecordClass recordClass, int timeToLive)
	{
		super(domainName, recordType, recordClass, timeToLive);
	}
	
	protected abstract int getAddressLength();
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(address).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		return new EqualsBuilder().append(address, ((AbstractAddressRecord)obj).address).isEquals();
	}

	@Override
	public void decodeData(IoBuffer byteBuffer, short length)
	{
		byte[] bytes = new byte[getAddressLength()];
		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = byteBuffer.get();
		}
		try
		{
			address = SimpleInetAddress.getByAddress(bytes);
		}
		catch (InvalidIPAddressFormatException e)
		{
			logger.debug("Failed to parse IP address: " + e.getLocalizedMessage(), e);
		}
		if (length != getAddressLength())
		{
			logger.warn("DNS '" + getRecordType().toString() + "' record with " + length + " of data. Very weird.");
		}
	}

	@Override
	public void encodeData(IoBuffer byteBuffer)
	{
		byteBuffer.put(address.getAddress());
	}

	public SimpleInetAddress getAddress()
	{
		return address;
	}

	public void setAddress(SimpleInetAddress address)
	{
		this.address = address;
	}

}
