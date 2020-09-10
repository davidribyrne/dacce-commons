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

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractAddressRecord.
 */
public abstract class AbstractAddressRecord extends ResourceRecord
{
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(AbstractAddressRecord.class);

	/** The address. */
	private SimpleInetAddress address;
	
	/**
	 * Instantiates a new abstract address record.
	 *
	 * @param domainName the domain name
	 * @param recordType the record type
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	protected AbstractAddressRecord(String domainName, RecordType recordType, RecordClass recordClass, int timeToLive)
	{
		super(domainName, recordType, recordClass, timeToLive);
	}
	
	/**
	 * Gets the address length.
	 *
	 * @return the address length
	 */
	protected abstract int getAddressLength();
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).appendSuper(super.toString()).append("address", address).build();
	}


	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(address).toHashCode();
	}


	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof AbstractAddressRecord))
			return false;
		if (obj == this)
			return true;
		
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(address, ((AbstractAddressRecord)obj).address).isEquals();
	}

	/**
	 * Decode data.
	 *
	 * @param byteBuffer the byte buffer
	 * @param length the length
	 */
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

	/**
	 * Encode data.
	 *
	 * @param byteBuffer the byte buffer
	 */
	@Override
	public void encodeData(IoBuffer byteBuffer)
	{
		byteBuffer.put(address.getAddress());
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public SimpleInetAddress getAddress()
	{
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(SimpleInetAddress address)
	{
		this.address = address;
	}

}
