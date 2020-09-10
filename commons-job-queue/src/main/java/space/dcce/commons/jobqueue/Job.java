package space.dcce.commons.jobqueue;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface Job.
 */
public interface Job extends Runnable
{
	
	/**
	 * Gets the hard prerequisites.
	 *
	 * @return the hard prerequisites
	 */
	public List<Job> getHardPrerequisites();
	
	/**
	 * Gets the soft prerequisites.
	 *
	 * @return the soft prerequisites
	 */
	public List<Job> getSoftPrerequisites();
	
	/**
	 * Gets the priority.
	 *
	 * @return the priority
	 */
	public int getPriority();
	
	/**
	 * Gets the job status.
	 *
	 * @return the job status
	 */
	public JobStatus getJobStatus();
}
