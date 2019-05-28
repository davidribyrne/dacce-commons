package space.dcce.commons.general;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Range 
{


	private final long start;
	private final long end;

	public Range(long start, long end)
	{
		this.start = start & 0xFFFFFFFFL;
		this.end = end & 0xFFFFFFFFL;
	}

	
	public Range(long value)
	{
		this.start = this.end = value;
	}
	
	
	public long size()
	{
		return end - start + 1;
	}

	/**
	 * 
	 * @param range
	 * @return True if any parts of the ranges overlap
	 */
	public boolean intersects(Range range)
	{
		return range.contains(start) || range.contains(end) || this.contains(range.start) || this.contains(range.end);
	}
	
	public boolean contains(long value)
	{
		return value >= start && value <= end;
	}
	
	
	@Override
	public String toString()
	{
		return start + "-" + end; 
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
		if (obj instanceof Range)
		{
			Range r = (Range) obj;
			return r.start == start && r.end == end;
		}
		return false;
	}

	public long getStart()
	{
		return start;
	}

//	public void setStart(long start)
//	{
//		this.start = start;
//	}

	public long getEnd()
	{
		return end;
	}

//	public void setEnd(long end)
//	{
//		this.end = end;
//	}




}
