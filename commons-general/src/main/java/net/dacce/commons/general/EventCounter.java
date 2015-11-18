package net.dacce.commons.general;

import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Should be thread-safe
 * @author dbyrne
 *
 */
public class EventCounter
{
	private final static Logger logger = LoggerFactory.getLogger(EventCounter.class);


	/** Length of time to monitor in ms */
	private final long masterDuration;
	private final LinkedList<Long> events;
	private boolean smooth;
	private long totalCount;
	
	/**
	 * 
	 * @param duration Length of time to monitor in ms
	 */
	public EventCounter(long duration, boolean smooth)
	{
		this.smooth = smooth;
		events = new LinkedList<Long>();
		this.masterDuration = duration;
	}

	/**
	 * Add an event that just happened
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
	
	public void waitForThrottle(int maxEvents) throws InterruptedException
	{
		waitForThrottle(maxEvents, 50);
	}
	
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
	 * Add an event at a specific time
	 * @param time
	 */
	public void trackEvent(long time)
	{
		
	}
}
