package net.dacce.commons.jobqueue;

import java.util.List;

public interface Job extends Runnable
{
	public List<Job> getHardPrerequisites();
	public List<Job> getSoftPrerequisites();
	public int getPriority();
	public JobStatus getJobStatus();
}
