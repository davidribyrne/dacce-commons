package space.dcce.commons.dns.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.mina.core.buffer.IoBuffer;

import space.dcce.commons.dns.io.DnsDecodingUtils;
import space.dcce.commons.dns.io.DnsEncodingUtils;
import space.dcce.commons.dns.messages.RecordClass;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractHostnameRecord.
 */
public abstract class AbstractHostnameRecord extends ResourceRecord
{

	/** The value. */
	private String value;
	
	/**
	 * Instantiates a new abstract hostname record.
	 *
	 * @param domainName the domain name
	 * @param recordType the record type
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	protected AbstractHostnameRecord(String domainName, RecordType recordType, RecordClass recordClass, int timeToLive)
	{
		super(domainName, recordType, recordClass, timeToLive);
	}


	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).appendSuper(super.toString()).append("value", value).build();
	}


	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(value).toHashCode();
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
		if (!(obj instanceof AbstractHostnameRecord))
			return false;
		if (obj == this)
			return true;
		
		AbstractHostnameRecord c = (AbstractHostnameRecord) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(value, c.value).isEquals();
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
		value = DnsDecodingUtils.getDomainName(byteBuffer);
	}


	/**
	 * Encode data.
	 *
	 * @param byteBuffer the byte buffer
	 */
	@Override
	protected void encodeData(IoBuffer byteBuffer)
	{
        DnsEncodingUtils.putDomainName( byteBuffer, value );
	}


	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}


	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

}
