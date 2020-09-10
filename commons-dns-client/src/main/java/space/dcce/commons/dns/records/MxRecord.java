package space.dcce.commons.dns.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.mina.core.buffer.IoBuffer;

import space.dcce.commons.dns.messages.RecordClass;

// TODO: Auto-generated Javadoc
/**
 * The Class MxRecord.
 */
public class MxRecord extends AbstractHostnameRecord
{
	
	/** The preference. */
	private int preference;

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(preference).appendSuper(super.hashCode()).toHashCode();
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
		if (!(obj instanceof MxRecord))
			return false;
		if (obj == this)
			return true;
		
		MxRecord o = (MxRecord) obj;
		return new EqualsBuilder().append(preference, o.preference).appendSuper(super.equals(obj)).isEquals();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).appendSuper(super.toString()).append("preference", preference).build();
	}

	/**
	 * Instantiates a new mx record.
	 *
	 * @param domainName the domain name
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	public MxRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.MX, recordClass, timeToLive);
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
        preference = byteBuffer.getShort() ;
        super.decodeData(byteBuffer, (short) (length - 2));
	}


	/**
	 * Encode data.
	 *
	 * @param byteBuffer the byte buffer
	 */
	@Override
	public void encodeData(IoBuffer byteBuffer)
	{
        byteBuffer.putShort( (short) preference );
        super.encodeData(byteBuffer);
	}
}
