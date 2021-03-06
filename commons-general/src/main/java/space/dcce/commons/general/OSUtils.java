package space.dcce.commons.general;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


// TODO: Auto-generated Javadoc
/**
 * The Class OSUtils.
 */
public class OSUtils
{

	/**
	 * Instantiates a new OS utils.
	 */
	private OSUtils()
	{
	}


	/**
	 * Checks if is windows.
	 *
	 * @return true, if is windows
	 */
	static public boolean isWindows()
	{
		return System.getProperty("os.name").toLowerCase().contains("windows");
	}


	/**
	 * Checks if is root.
	 *
	 * @return true if process is running as root or administrator
	 */
	static public boolean isRoot()
	{
		Preferences prefs = Preferences.systemRoot();
		try
		{
			// https://stackoverflow.com/questions/4350356/detect-if-java-application-was-run-as-a-windows-admin
			prefs.put("foo", "bar"); // SecurityException on Windows
			prefs.remove("foo");
			prefs.flush(); // BackingStoreException on Linux
			return true;
		}
		catch (SecurityException e)
		{
			// Not a Windows admin
			return false;
		}
		catch (BackingStoreException e)
		{
			// Not root
			return false;
		}

	}


}
