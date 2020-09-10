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
 * The Class AbstractStringRecord.
 */
public abstract class AbstractStringRecord extends ResourceRecord
{
	
	/** The data. */
	private String data;

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().appendSuper(super.hashCode()).append(data).toHashCode();
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
		if (!(obj instanceof AbstractStringRecord))
			return false;
		if (obj == this)
			return true;
					
		AbstractStringRecord a = (AbstractStringRecord) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(a.data, data).isEquals();
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).appendSuper(super.toString()).append("data", data).build();
	}


	/**
	 * Instantiates a new abstract string record.
	 *
	 * @param domainName the domain name
	 * @param recordType the record type
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	public AbstractStringRecord(String domainName, RecordType recordType, RecordClass recordClass, int timeToLive)
	{
		super(domainName, recordType, recordClass, timeToLive);
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
		data = DnsDecodingUtils.getCharacterString(byteBuffer, length);
	}


	/**
	 * Encode data.
	 *
	 * @param byteBuffer the byte buffer
	 */
	@Override
	protected void encodeData(IoBuffer byteBuffer)
	{
		DnsEncodingUtils.putCharacterString(byteBuffer, data);
	}


	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public String getData()
	{
		return data;
	}


	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(String data)
	{
		this.data = data;
	}
}
