package space.dcce.commons.general;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;


import java.util.*;


// TODO: Auto-generated Javadoc
/**
 * The Class RangeSet.
 */
public class RangeSet
{
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(RangeSet.class);

	/** The original ranges. */
	private final List<Range> originalRanges;
	
	/** The flattened ranges. */
	private List<Range> flattenedRanges;
	
	/** The changed. */
	private boolean changed;
	
	/** The size. */
	protected int size;
	
	/** The name. */
	private final String name;


	/**
	 * Instantiates a new range set.
	 *
	 * @param name the name
	 */
	public RangeSet(String name)
	{
		this.name = name;
		originalRanges = new ArrayList<Range>();
		flattenedRanges = Collections.EMPTY_LIST;
	}


	/**
	 * Instantiates a new range set.
	 *
	 * @param name the name
	 * @param ranges the ranges
	 * @param flattenFirst if true, combine overlapping or adjacent ranges; don't track original ranges
	 */
	public RangeSet(String name, List<Range> ranges, boolean flattenFirst)
	{
		this(name);
		if (flattenFirst)
		{
			ranges = flattenRanges(ranges);
		}
		originalRanges.addAll(ranges);
		changed = true;
	}


	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size()
	{
		synchronized (this)
		{
			checkRebuild();
			return size;
		}
	}


	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty()
	{
		return originalRanges.isEmpty();
	}


	/**
	 * Returns a union of both ranges; basically, a logical OR.
	 *
	 * @param target the target
	 * @return the range set
	 */
	public RangeSet union(RangeSet target)
	{
		UniqueList<Range> union = new UniqueList<Range>(false);
		union.addAll(originalRanges);
		union.addAll(target.originalRanges);
		List<Range> flatUnion = flattenRanges(union);
		return new RangeSet("Union of " + name + " and " + target.name, flatUnion, true);
	}


	/**
	 * Complement.
	 *
	 * @return A logical NOT
	 */
	public RangeSet complement()
	{
		List<Range> complement = new ArrayList<Range>();
		List<Range> flattened = getFlattendRanges();

		long lastEnd = Long.MIN_VALUE;

		for (int i = 0; i < flattened.size(); i++)
		{
			Range r = flattened.get(i);
			complement.add(new Range(lastEnd + 1, r.getStart() - 1));
			lastEnd = r.getEnd();
		}
		complement.add(new Range(lastEnd + 1, Long.MAX_VALUE));
		return new RangeSet("complement of " + name, complement, true);
	}


	/**
	 * Difference.
	 *
	 * @param target the target
	 * @return the range set
	 */
	public RangeSet difference(RangeSet target)
	{
		return intersection(target.complement());
	}


	/**
	 * Returns a RangeSet of ranges of values that are in both sets;
	 * basically a logical AND.
	 *
	 * @param target the target
	 * @return the range set
	 */
	public RangeSet intersection(RangeSet target)
	{
		
		if (isEmpty() || target.isEmpty())
		{
			return new RangeSet("Intersection of " + name + " and " + target.name);
		}
		
		List<Range> intersection = new ArrayList<Range>();

		List<Range> rangesA = getFlattendRanges();
		List<Range> rangesB = target.getFlattendRanges();

		Iterator<Range> itoratorA = rangesA.iterator();
		Iterator<Range> itoratorB = rangesB.iterator();

		Range rangeA = null; 
		Range rangeB = null;

		while (itoratorA.hasNext() || itoratorB.hasNext())
		{
			if (rangeA == null)
			{
				rangeA = itoratorA.next();
				rangeB = itoratorB.next();
			}
			else if (rangeA.getEnd() < rangeB.getEnd())
			{
				if (itoratorA.hasNext())
				{
					rangeA = itoratorA.next();
				}
				else if (itoratorB.hasNext())
				{
					rangeB = itoratorB.next();
				}
			}
			else if (rangeA.getEnd() > rangeB.getEnd())
			{
				if (itoratorB.hasNext())
				{
					rangeB = itoratorB.next();
				}
				else if (itoratorA.hasNext())
				{
					rangeA = itoratorA.next();
				}
			}
			// else, the ends are the same
			else
			{
				if (itoratorA.hasNext())
				{
					rangeA = itoratorA.next();
				}
				if (itoratorB.hasNext())
				{
					rangeB = itoratorB.next();
				}
			}
			
			
			if (rangeA.intersects(rangeB))
			{
				Range rangeC = new Range(Math.max(rangeA.getStart(), rangeB.getStart()), Math.min(rangeA.getEnd(), rangeB.getEnd()));
				intersection.add(rangeC);
			}

		}

		return new RangeSet("Intersection of " + name + " and " + target.name, intersection, true);
	}


	/**
	 * Gets the flattend ranges.
	 *
	 * @return Sorted
	 */
	public List<Range> getFlattendRanges()
	{
		synchronized (this)
		{
			checkRebuild();
			return flattenedRanges;
		}
	}


	/**
	 * Check rebuild.
	 */
	protected final synchronized void checkRebuild()
	{
		if (changed)
		{
			Collections.sort(originalRanges, startComparator);
			flattenedRanges = flattenRanges(originalRanges);
			size = 0;
			for (Range range : flattenedRanges)
			{
				size += range.size();
			}
			changed = false;
		}
	}


	/** The start comparator. */
	private static Comparator<Range> startComparator = new Comparator<Range>()
	{
		@Override
		public int compare(Range o1, Range o2)
		{
			return Long.compare(o1.getStart(), o2.getStart());
		}
	};


	/**
	 * Contains.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean contains(long value)
	{
		int i = Collections.binarySearch(getFlattendRanges(), new Range(value), startComparator);
		return i > 0;
	}


	/**
	 * Flatten ranges.
	 *
	 * @param originalRanges the original ranges
	 * @return the list
	 */
	private static List<Range> flattenRanges(List<Range> originalRanges)
	{
		List<Range> flattenedRanges = new ArrayList<Range>();
		Collections.sort(originalRanges, startComparator);
		for (Range range : originalRanges)
		{
			if (flattenedRanges.size() == 0)
			{
				flattenedRanges.add(range);
			}
			else
			{
				Range flatRange = Iterables.getLast(flattenedRanges);

				/*
				 * If the original range start is in the flat range or adjacent, but the end
				 * extends beyond the current flattened range, extend the flattend range
				 */
				if ((flatRange.contains(range.getStart()) && range.getEnd() > flatRange.getEnd()) ||
						flatRange.getEnd() + 1 == range.getStart())
				{
					flattenedRanges.remove(flatRange);
					flattenedRanges.add(new Range(flatRange.getStart(), range.getEnd()));
				}

				// If the original range is outside the current flat range
				else if (range.getStart() > flatRange.getEnd())
				{
					flattenedRanges.add(range);
				}
				else if (range.getStart() >= flatRange.getStart() && range.getEnd() <= flatRange.getEnd())
				{
//					logger.warn("range is completely contained in flat range");
				}
				else
				{
					logger.warn("what?");
				}
			}
		}
		Collections.sort(flattenedRanges, startComparator);

		return Collections.unmodifiableList(flattenedRanges);
	}


	/**
	 * Adds the.
	 *
	 * @param range the range
	 */
	public void add(Range range)
	{
		synchronized (this)
		{
			originalRanges.add(range);
			changed = true;
		}
	}


	/**
	 * Adds the all.
	 *
	 * @param ranges the ranges
	 */
	public void addAll(Collection<Range> ranges)
	{
		synchronized (this)
		{
			originalRanges.addAll(ranges);
			changed = true;
		}
	}


	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString()
	{
		checkRebuild();
		return CollectionUtils.joinObjects(", ", originalRanges);
	}


	/**
	 * Gets the original ranges.
	 *
	 * @return the original ranges
	 */
	public List<Range> getOriginalRanges()
	{
		checkRebuild();
		return Collections.unmodifiableList(originalRanges);
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

}
