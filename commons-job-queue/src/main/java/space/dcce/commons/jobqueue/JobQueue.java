package space.dcce.commons.jobqueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

// TODO: Auto-generated Javadoc
/**
 * The Class JobQueue.
 */
public class JobQueue implements BlockingQueue<Runnable>
{
	
	/** The jobs. */
	private final List<Job> jobs;

	/**
	 * Instantiates a new job queue.
	 */
	public JobQueue()
	{
		jobs = new ArrayList<Job>();
	}

	/**
	 * Unsynched get next job.
	 *
	 * @param remove the remove
	 * @return the job
	 */
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

	/**
	 * Checks if is job ready.
	 *
	 * @param job the job
	 * @return true, if is job ready
	 */
	private boolean isJobReady(Job job)
	{
		return checkPrerequisites(job.getHardPrerequisites(), true) && checkPrerequisites(job.getSoftPrerequisites(), false);
	}

	/**
	 * Check prerequisites.
	 *
	 * @param prerequisites the prerequisites
	 * @param hard the hard
	 * @return true, if successful
	 */
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


	/**
	 * Removes the.
	 *
	 * @return the job
	 * @throws NoSuchElementException the no such element exception
	 */
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

	/**
	 * Poll.
	 *
	 * @return the job
	 */
	@Override
	public Job poll()
	{
		synchronized (jobs)
		{
			return unsynchedGetNextJob(true);
		}
	}

	/**
	 * Element.
	 *
	 * @return the job
	 * @throws NoSuchElementException the no such element exception
	 */
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

	/**
	 * Peek.
	 *
	 * @return the job
	 */
	@Override
	public Job peek()
	{
		synchronized (jobs)
		{
			return unsynchedGetNextJob(false);
		}
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	@Override
	public int size()
	{
		synchronized (jobs)
		{
			return jobs.size();
		}
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	@Override
	public boolean isEmpty()
	{
		synchronized (jobs)
		{

			return jobs.isEmpty();
		}
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Iterator iterator()
	{
		synchronized (jobs)
		{
			return jobs.iterator();
		}
	}

	/**
	 * To array.
	 *
	 * @return the object[]
	 */
	@Override
	public Object[] toArray()
	{
		synchronized (jobs)
		{
			return jobs.toArray();
		}
	}

	/**
	 * To array.
	 *
	 * @param <T> the generic type
	 * @param a the a
	 * @return the t[]
	 */
	@Override
	public <T> T[] toArray(T[] a)
	{
		synchronized (jobs)
		{
			return jobs.toArray(a);
		}
	}

	/**
	 * Contains all.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		synchronized (jobs)
		{
			return jobs.containsAll(c);
		}
	}

	/**
	 * Adds the all.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
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

	/**
	 * Removes the all.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		synchronized (jobs)
		{
			return jobs.removeAll(c);
		}
	}

	/**
	 * Retain all.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		synchronized (jobs)
		{
			return jobs.retainAll(c);
		}
	}

	/**
	 * Clear.
	 */
	@Override
	public void clear()
	{
		synchronized (jobs)
		{
			jobs.clear();
		}
	}

	/**
	 * Adds the.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	@Override
	public boolean add(Runnable e)
	{
		return addAll(Collections.singletonList(e));
	}

	/**
	 * Offer.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	@Override
	public boolean offer(Runnable e)
	{
		return add(e);
	}

	/**
	 * Put.
	 *
	 * @param e the e
	 * @throws InterruptedException the interrupted exception
	 */
	@Override
	public void put(Runnable e) throws InterruptedException
	{
		add(e);
	}

	/**
	 * Offer.
	 *
	 * @param e the e
	 * @param timeout the timeout
	 * @param unit the unit
	 * @return true, if successful
	 * @throws InterruptedException the interrupted exception
	 */
	@Override
	public boolean offer(Runnable e, long timeout, TimeUnit unit) throws InterruptedException
	{
		return add(e);
	}

	/**
	 * Take.
	 *
	 * @return the job
	 * @throws InterruptedException the interrupted exception
	 */
	@Override
	public Job take() throws InterruptedException
	{
		return poll(Long.MAX_VALUE, TimeUnit.DAYS);
	}

	/**
	 * Poll.
	 *
	 * @param timeout the timeout
	 * @param unit the unit
	 * @return the job
	 * @throws InterruptedException the interrupted exception
	 */
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

	/**
	 * Remaining capacity.
	 *
	 * @return the int
	 */
	@Override
	public int remainingCapacity()
	{
		return Integer.MAX_VALUE;
	}

	/**
	 * Removes the.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	@Override
	public boolean remove(Object o)
	{
		synchronized (jobs)
		{
			return jobs.remove(o);
		}
	}

	/**
	 * Contains.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	@Override
	public boolean contains(Object o)
	{
		synchronized (jobs)
		{
			return jobs.contains(o);
		}
	}

	/**
	 * Drain to.
	 *
	 * @param c the c
	 * @return the int
	 */
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

	/**
	 * Drain to.
	 *
	 * @param c the c
	 * @param maxElements the max elements
	 * @return the int
	 */
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
