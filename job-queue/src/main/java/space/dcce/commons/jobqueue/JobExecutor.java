package space.dcce.commons.jobqueue;

import java.util.Collection;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JobExecutor extends ThreadPoolExecutor
{
	public JobExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new JobQueue());
	}

	public JobQueue getQueue()
	{
		return (JobQueue) super.getQueue();
	}
	
	public boolean addAll(Collection<? extends Job> c)
	{
		return getQueue().addAll(c);
	}

	public boolean add(Job e)
	{
		return getQueue().add(e);
	}

}
