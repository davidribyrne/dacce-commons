//package space.dcce.commons.netaddr;
//
//import org.apache.commons.lang3.builder.HashCodeBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import space.dcce.commons.general.Range;
//import space.dcce.commons.general.UnexpectedException;
//
//import java.util.*;
//
//public class IP4Range extends Range
//{
//
//	public IP4Range(int start, int end)
//	{
//		super(start, end);
//	}
//
//	public IP4Range(int value)
//	{
//		super(value);
//	}
//
//	@Override
//	public String toString()
//	{
//		if (getEnd() == getStart())
//			return IP4Utils.intToString(getStart());
//		return IP4Utils.intToString(getStart()) + "-" + IP4Utils.intToString(getEnd()); 
//	}
//
//	@Override
//	public boolean equals(Object obj)
//	{
//		if (obj instanceof IP4Range)
//		{
//			return super.equals(obj);
//		}
//		return false;
//	}
//
//}
