package space.dcce.commons.jobqueue;

import java.util.Collection;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// TODO: Auto-generated Javadoc
/**
 * The Class JobExecutor.
 */
public class JobExecutor extends ThreadPoolExecutor
{
	
	/**
	 * Instantiates a new job executor.
	 *
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @param keepAliveTime the keep alive time
	 * @param unit the unit
	 */
	public JobExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new JobQueue());
	}

	/**
	 * Gets the queue.
	 *
	 * @return the queue
	 */
	public JobQueue getQueue()
	{
		return (JobQueue) super.getQueue();
	}
	
	/**
	 * Adds the all.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	public boolean addAll(Collection<? extends Job> c)
	{
		return getQueue().addAll(c);
	}

	/**
	 * Adds the.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	public boolean add(Job e)
	{
		return getQueue().add(e);
	}

}
