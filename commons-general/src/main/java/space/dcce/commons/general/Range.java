package space.dcce.commons.general;

import org.apache.commons.lang3.builder.HashCodeBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class Range.
 */
public class Range 
{


	/** The start. */
	private final long start;
	
	/** The end. */
	private final long end;

	/**
	 * Instantiates a new range.
	 *
	 * @param start the start
	 * @param end the end
	 */
	public Range(long start, long end)
	{
		this.start = start & 0xFFFFFFFFL;
		this.end = end & 0xFFFFFFFFL;
	}

	
	/**
	 * Instantiates a new range.
	 *
	 * @param value the value
	 */
	public Range(long value)
	{
		this.start = this.end = value;
	}
	
	
	/**
	 * Size.
	 *
	 * @return the long
	 */
	public long size()
	{
		return end - start + 1;
	}

	/**
	 * Intersects.
	 *
	 * @param range the range
	 * @return True if any parts of the ranges overlap
	 */
	public boolean intersects(Range range)
	{
		return range.contains(start) || range.contains(end) || this.contains(range.start) || this.contains(range.end);
	}
	
	/**
	 * Contains.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean contains(long value)
	{
		return value >= start && value <= end;
	}
	
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		return start + "-" + end; 
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode()
	{
		HashCodeBuilder hcb = new HashCodeBuilder();
		return hcb.append(start).append(end).toHashCode();
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
		if (obj instanceof Range)
		{
			Range r = (Range) obj;
			return r.start == start && r.end == end;
		}
		return false;
	}

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public long getStart()
	{
		return start;
	}

//	public void setStart(long start)
//	{
//		this.start = start;
//	}

	/**
 * Gets the end.
 *
 * @return the end
 */
public long getEnd()
	{
		return end;
	}

//	public void setEnd(long end)
//	{
//		this.end = end;
//	}




}
