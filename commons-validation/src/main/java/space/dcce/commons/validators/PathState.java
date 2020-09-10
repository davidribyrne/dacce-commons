package space.dcce.commons.validators;


// TODO: Auto-generated Javadoc
/**
 * The Class PathState.
 */
public class PathState
{
	
	/**
	 * @param executable
	 * @param readable
	 * @param writeable
	 * @param directory
	 * @param exists
	 */
	public PathState(Requirement executable, Requirement readable, Requirement writeable, Requirement directory, Requirement exists)
	{
		this.executable = executable;
		this.readable = readable;
		this.writeable = writeable;
		this.directory = directory;
		this.exists = exists;
	}

	/**
	 * Instantiates a new path state.
	 */
	public PathState()
	{
		
	}
	
	/** The executable. */
	public Requirement executable = Requirement.DOES_NOT_MATTER;
	
	/** The readable. */
	public Requirement readable = Requirement.DOES_NOT_MATTER;
	
	/** The writeable. */
	public Requirement writeable = Requirement.DOES_NOT_MATTER;
	
	/** The directory. */
	public Requirement directory = Requirement.DOES_NOT_MATTER;
	
	/** The exists. */
	public Requirement exists = Requirement.DOES_NOT_MATTER;
	
}
