package space.dcce.commons.general;

import java.util.Iterator;
import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * Should be thread-safe.
 *
 * @author dbyrne
 */
public class EventCounter
{
	
	/**  Length of time to monitor in ms. */
	private final long masterDuration;
	
	/** The events. */
	private final LinkedList<Long> events;
	
	/** The smooth. */
	private boolean smooth;
	
	/** The total count. */
	private long totalCount;
	
	/**
	 * Instantiates a new event counter.
	 *
	 * @param duration Length of time to monitor in ms
	 * @param smooth the smooth
	 */
	public EventCounter(long duration, boolean smooth)
	{
		this.smooth = smooth;
		events = new LinkedList<Long>();
		this.masterDuration = duration;
	}

	/**
	 * Add an event that just happened.
	 */
	public void trackEvent()
	{
		long time = System.currentTimeMillis();
		synchronized(events)
		{
			totalCount++;
			events.add(time);
		}
	}
	
	/**
	 * Purge.
	 */
	private void purge()
	{
		synchronized(events)
		{
			long oldestTime = System.currentTimeMillis() - masterDuration;
			for (Iterator<Long> iterator = events.iterator(); iterator.hasNext();)
			{
				if(iterator.next() < oldestTime)
					iterator.remove();
			}
		}
	}
	
	/**
	 * Wait for throttle.
	 *
	 * @param maxEvents the max events
	 * @throws InterruptedException the interrupted exception
	 */
	public void waitForThrottle(int maxEvents) throws InterruptedException
	{
		waitForThrottle(maxEvents, 50);
	}
	
	/**
	 * Wait for throttle.
	 *
	 * @param maxEvents the max events
	 * @param sleepIncrement the sleep increment
	 * @throws InterruptedException the interrupted exception
	 */
	public void waitForThrottle(int maxEvents, int sleepIncrement) throws InterruptedException
	{
		Object o = new Object();
		double multiplier = 1;
		if (smooth && maxEvents >= 50 && masterDuration >= 500)
		{
			multiplier = 0.2;
			
		}
		synchronized(o)
		{
			synchronized(events)
			{
				purge();
			}
			while(getTrackedEventCount((long) (masterDuration * multiplier)) >= maxEvents * multiplier)
			{
				o.wait(sleepIncrement);
			}
		}
	}
	
	/**
	 * Gets the tracked event count.
	 *
	 * @param duration the duration
	 * @return the tracked event count
	 */
	public int getTrackedEventCount(long duration)
	{
		int count = 0;
		synchronized(events)
		{
			long oldestTime = System.currentTimeMillis() - duration;
			for (Iterator<Long> iterator = events.iterator(); iterator.hasNext();)
			{
				if(iterator.next() > oldestTime)
					count++;
			}
		}
		return count;
	}

	/**
	 * Gets the total count.
	 *
	 * @return the total count
	 */
	public long getTotalCount()
	{
		return totalCount;
	}
	
	
//	
//	/**
//	 * Add an event at a specific time
//	 * @param time
//	 */
//	public void trackEvent(long time)
//	{
//		
//	}
}
