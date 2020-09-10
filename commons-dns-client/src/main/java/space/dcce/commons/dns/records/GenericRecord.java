package space.dcce.commons.dns.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.mina.core.buffer.IoBuffer;

import space.dcce.commons.dns.messages.RecordClass;

// TODO: Auto-generated Javadoc
/**
 * The Class GenericRecord.
 */
public class GenericRecord extends ResourceRecord
{
	
	/** The data. */
	private byte[] data;
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}


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
		if (!(obj instanceof GenericRecord))
			return false;
		if (obj == this)
			return true;
		
		GenericRecord o = (GenericRecord) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(data, o.data).isEquals();
	}


	/**
	 * Instantiates a new generic record.
	 *
	 * @param domainName the domain name
	 * @param recordType the record type
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	public GenericRecord(String domainName, RecordType recordType, RecordClass recordClass, int timeToLive)
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
		data = new byte[length];
		byteBuffer.get(data);
	}


	/**
	 * Encode data.
	 *
	 * @param byteBuffer the byte buffer
	 */
	@Override
	protected void encodeData(IoBuffer byteBuffer)
	{
		byteBuffer.put(data);
	}


	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public byte[] getData()
	{
		return data;
	}


	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(byte[] data)
	{
		this.data = data;
	}
}
