package net.dacce.commons.validators;

import java.io.File;
import java.io.IOException;



public class PathValidator implements Validator
{

	public PathState state;



	public PathValidator(PathState state)
	{
		this.state = state;
	}


	public PathValidator()
	{
		state = new PathState();
	}


	@Override
	public void validate(String value) throws ValidationException
	{
		String errorPrefix = "Path '" + value + "' ";
		File f = new File(value);
		try
		{
			f.getCanonicalPath();
		}
		catch (IOException e)
		{
			throw new ValidationException(errorPrefix + "does not appear valid.", e);
		}
		
		if (!f.exists())
		{
			if (state.exists == Requirement.MUST)
			{
				throw new ValidationException(errorPrefix + "does not exist.");
			}
		}
		else // does exist
		{
			if (state.exists == Requirement.MUST_NOT)
			{
				throw new ValidationException(errorPrefix + "exists.");
			}

			
			// These aren't the most efficient conditionals, but they are movable and more readable
			
			if (f.isDirectory() && state.directory == Requirement.MUST_NOT)
			{
				throw new ValidationException(errorPrefix + "is a directory.");
			}
			
			if (!f.isDirectory() && state.directory == Requirement.MUST)
			{
				throw new ValidationException(errorPrefix + "is not a directory.");
			}


			if (f.canExecute() && state.executable == Requirement.MUST_NOT)
			{
				throw new ValidationException(errorPrefix + "is executable.");
			}

			if (!f.canExecute() && state.executable == Requirement.MUST)
			{
				throw new ValidationException(errorPrefix + "is not executable.");
			}


			if (f.canRead() && state.readable == Requirement.MUST_NOT)
			{
				throw new ValidationException(errorPrefix + "is readable.");
			}

			if (!f.canRead() && state.readable == Requirement.MUST)
			{
				throw new ValidationException(errorPrefix + "is not readable.");
			}


			if (f.canWrite() && state.writeable == Requirement.MUST_NOT)
			{
				throw new ValidationException(errorPrefix + "is writeable.");
			}

			if (!f.canWrite() && state.writeable == Requirement.MUST)
			{
				throw new ValidationException(errorPrefix + "is not writeable.");
			}

		}
		
	}
}
