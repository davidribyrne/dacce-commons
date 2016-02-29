package net.dacce.commons.dns.records;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.mina.core.buffer.IoBuffer;
import net.dacce.commons.dns.io.DnsDecodingUtils;
import net.dacce.commons.dns.io.DnsEncodingUtils;
import net.dacce.commons.dns.messages.RecordClass;

public class SrvRecord extends ResourceRecord
{
	private int priority;
	private int weight;
	private int port;
	private String name;

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(priority).append(weight).append(port).append(name)
				.appendSuper(super.hashCode()).toHashCode();
	}


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


	public SrvRecord(String domainName, RecordClass recordClass, int timeToLive)
	{
		super(domainName, RecordType.SRV, recordClass, timeToLive);
	}


	@Override
	public void decodeData(IoBuffer byteBuffer, short length)
	{
		priority = byteBuffer.getInt();
		weight = byteBuffer.getInt();
		port = byteBuffer.getInt();
		name = DnsDecodingUtils.getDomainName(byteBuffer);
	}


	@Override
	protected void encodeData(IoBuffer byteBuffer)
	{
		byteBuffer.putShort((short) priority);
		byteBuffer.putShort((short) weight);
		byteBuffer.putShort((short) port);
		DnsEncodingUtils.putDomainName(byteBuffer, name);
	}


	public int getPriority()
	{
		return priority;
	}


	public void setPriority(int priority)
	{
		this.priority = priority;
	}


	public int getWeight()
	{
		return weight;
	}


	public void setWeight(int weight)
	{
		this.weight = weight;
	}


	public int getPort()
	{
		return port;
	}


	public void setPort(int port)
	{
		this.port = port;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}
}
