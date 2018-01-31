package space.dcce.commons.dns.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.dcce.commons.dns.messages.RecordClass;
import space.dcce.commons.netaddr.InvalidIPAddressFormatException;
import space.dcce.commons.netaddr.SimpleInetAddress;

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
		return new ToStringBuilder(this).appendSuper(super.toString()).append("address", address).build();
	}


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(address).toHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof AbstractAddressRecord))
			return false;
		if (obj == this)
			return true;
		
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(address, ((AbstractAddressRecord)obj).address).isEquals();
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
