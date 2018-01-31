package space.dcce.commons.jobqueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class JobQueue implements BlockingQueue<Runnable>
{
	private final List<Job> jobs;

	public JobQueue()
	{
		jobs = new ArrayList<Job>();
	}

	private Job unsynchedGetNextJob(boolean remove)
	{
		Job currentCandidate = null;
		for (Job job : jobs)
		{
			if (currentCandidate == null || (isJobReady(job) && job.getPriority() > currentCandidate.getPriority()))
				currentCandidate = job;
		}
		if (remove && currentCandidate != null)
			jobs.remove(currentCandidate);
		return currentCandidate;
	}

	private boolean isJobReady(Job job)
	{
		return checkPrerequisites(job.getHardPrerequisites(), true) && checkPrerequisites(job.getSoftPrerequisites(), false);
	}

	private boolean checkPrerequisites(List<Job> prerequisites, boolean hard)
	{
		if (prerequisites == null || prerequisites.isEmpty())
			return true;
		
		for (Job prerequisite : prerequisites)
		{
			if (prerequisite.getJobStatus().equals(JobStatus.UNSTARTED) ||
					prerequisite.getJobStatus().equals(JobStatus.RUNNING))
				return false;

			if (hard && prerequisite.getJobStatus().equals(JobStatus.FAILED))
				return false;
		}
		return true;
	}


	@Override
	public Job remove() throws NoSuchElementException
	{
		synchronized (jobs)
		{
			Job job = unsynchedGetNextJob(true);
			if (job == null)
				throw new NoSuchElementException();
			return job;
		}
	}

	@Override
	public Job poll()
	{
		synchronized (jobs)
		{
			return unsynchedGetNextJob(true);
		}
	}

	@Override
	public Job element() throws NoSuchElementException
	{
		synchronized (jobs)
		{
			Job job = unsynchedGetNextJob(false);
			if (job == null)
				throw new NoSuchElementException();
			return job;
		}
	}

	@Override
	public Job peek()
	{
		synchronized (jobs)
		{
			return unsynchedGetNextJob(false);
		}
	}

	@Override
	public int size()
	{
		synchronized (jobs)
		{
			return jobs.size();
		}
	}

	@Override
	public boolean isEmpty()
	{
		synchronized (jobs)
		{

			return jobs.isEmpty();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Iterator iterator()
	{
		synchronized (jobs)
		{
			return jobs.iterator();
		}
	}

	@Override
	public Object[] toArray()
	{
		synchronized (jobs)
		{
			return jobs.toArray();
		}
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		synchronized (jobs)
		{
			return jobs.toArray(a);
		}
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		synchronized (jobs)
		{
			return jobs.containsAll(c);
		}
	}

	@Override
	public boolean addAll(Collection<? extends Runnable> c)
	{
		boolean result = false;
		synchronized (jobs)
		{
			for(Runnable r: c)
			{
				if (! (c instanceof Job))
				{
					throw new IllegalArgumentException("JobQueue will only take Job objects.");
				}
				result = result || jobs.add((Job) r);
			}
			return result;
		}
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		synchronized (jobs)
		{
			return jobs.removeAll(c);
		}
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		synchronized (jobs)
		{
			return jobs.retainAll(c);
		}
	}

	@Override
	public void clear()
	{
		synchronized (jobs)
		{
			jobs.clear();
		}
	}

	@Override
	public boolean add(Runnable e)
	{
		return addAll(Collections.singletonList(e));
	}

	@Override
	public boolean offer(Runnable e)
	{
		return add(e);
	}

	@Override
	public void put(Runnable e) throws InterruptedException
	{
		add(e);
	}

	@Override
	public boolean offer(Runnable e, long timeout, TimeUnit unit) throws InterruptedException
	{
		return add(e);
	}

	@Override
	public Job take() throws InterruptedException
	{
		return poll(Long.MAX_VALUE, TimeUnit.DAYS);
	}

	@Override
	public Job poll(long timeout, TimeUnit unit) throws InterruptedException
	{
		long msecs = TimeUnit.MILLISECONDS.convert(timeout, unit);
		long start = System.currentTimeMillis();
		Job job;
		synchronized (jobs)
		{
			do
			{
				job = unsynchedGetNextJob(true);
			}
			while (job == null && System.currentTimeMillis() - start < msecs);
		}
		return job;
	}

	@Override
	public int remainingCapacity()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean remove(Object o)
	{
		synchronized (jobs)
		{
			return jobs.remove(o);
		}
	}

	@Override
	public boolean contains(Object o)
	{
		synchronized (jobs)
		{
			return jobs.contains(o);
		}
	}

	@Override
	public int drainTo(Collection<? super Runnable> c)
	{
		synchronized (jobs)
		{
			int size = jobs.size();
			c.addAll(jobs);
			jobs.clear();
			return size;
		}
	}

	@Override
	public int drainTo(Collection<? super Runnable> c, int maxElements)
	{
		synchronized (jobs)
		{
			int count = 0;
			Job job;
			do
			{
				job = unsynchedGetNextJob(true);
				c.add(job);
			}
			while (job != null && ++count <= maxElements);
			return count;
		}
	}


}
