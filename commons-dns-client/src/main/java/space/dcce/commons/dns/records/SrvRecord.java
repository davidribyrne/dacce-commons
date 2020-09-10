package space.dcce.commons.dns.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.mina.core.buffer.IoBuffer;

import space.dcce.commons.dns.io.DnsDecodingUtils;
import space.dcce.commons.dns.io.DnsEncodingUtils;
import space.dcce.commons.dns.messages.RecordClass;

// TODO: Auto-generated Javadoc
/**
 * The Class SrvRecord.
 */
public class SrvRecord extends ResourceRecord
{
	
	/** The priority. */
	private int priority;
	
	/** The weight. */
	private int weight;
	
	/** The port. */
	private int port;
	
	/** The name. */
	private String name;

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(priority).append(weight).append(port).append(name)
				.appendSuper(super.hashCode()).toHashCode();
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
		if (!(obj instanceof SrvRecord))
			return false;
		if (obj == this)
			return true;
		
		SrvRecord s = (SrvRecord) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(priority, s.priority)
				.append(weight, s.weight).append(port, s.port).
				append(name, s.name).isEquals();
	}


	/**
	 * Instantiates a new srv record.
	 *
	 * @param domainName the domain name
	 * @param recordClass the record class
	 * @param timeToLive the time to live
	 */
	public SrvRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.SRV, recordClass, timeToLive);
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
		priority = byteBuffer.getInt();
		weight = byteBuffer.getInt();
		port = byteBuffer.getInt();
		name = DnsDecodingUtils.getDomainName(byteBuffer);
	}


	/**
	 * Encode data.
	 *
	 * @param byteBuffer the byte buffer
	 */
	@Override
	protected void encodeData(IoBuffer byteBuffer)
	{
		byteBuffer.putShort((short) priority);
		byteBuffer.putShort((short) weight);
		byteBuffer.putShort((short) port);
		DnsEncodingUtils.putDomainName(byteBuffer, name);
	}


	/**
	 * Gets the priority.
	 *
	 * @return the priority
	 */
	public int getPriority()
	{
		return priority;
	}


	/**
	 * Sets the priority.
	 *
	 * @param priority the new priority
	 */
	public void setPriority(int priority)
	{
		this.priority = priority;
	}


	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public int getWeight()
	{
		return weight;
	}


	/**
	 * Sets the weight.
	 *
	 * @param weight the new weight
	 */
	public void setWeight(int weight)
	{
		this.weight = weight;
	}


	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort()
	{
		return port;
	}


	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(int port)
	{
		this.port = port;
	}


	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
