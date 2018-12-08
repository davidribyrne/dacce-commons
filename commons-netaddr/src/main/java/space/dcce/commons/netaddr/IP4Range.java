package space.dcce.commons.netaddr;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.dcce.commons.general.UnexpectedException;

import java.util.*;

public class IP4Range
{
	private final static Logger logger = LoggerFactory.getLogger(IP4Range.class);

	private final int start;
	private final int end;

	
	public IP4Range(int start, int end)
	{
		this.start = start;
		this.end = end;
	}
	
	public IP4Range(SimpleInet4Address address)
	{
		try
		{
			int i = IP4Utils.octetsToInt(address.getAddress());
			start = end = i;
		}
		catch (InvalidIPAddressFormatException e)
		{
			throw new UnexpectedException(e);
		}
	}
	
	public int size()
	{
		return end - start + 1;
	}
	
	public boolean contains(SimpleInet4Address address)
	{
		try
		{
			int i = IP4Utils.octetsToInt(address.getAddress());
			return(contains(i));
		}
		catch (InvalidIPAddressFormatException e)
		{
			throw new UnexpectedException(e);
		}
		
	}
	
	public boolean contains(int address)
	{
		return address >= start && address <= end;
	}
	
	
	@Override
	public String toString()
	{
		if (end == start)
			return IP4Utils.decimalToString(start);
		return IP4Utils.decimalToString(start) + "-" + IP4Utils.decimalToString(end); 
	}

	@Override
	public int hashCode()
	{
		HashCodeBuilder hcb = new HashCodeBuilder();
		return hcb.append(start).append(end).toHashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof IP4Range)
		{
			IP4Range r = (IP4Range) obj;
			return r.start == start && r.end == end;
		}
		return false;
	}

	public int getStart()
	{
		return start;
	}

	public int getEnd()
	{
		return end;
	}
	
}
