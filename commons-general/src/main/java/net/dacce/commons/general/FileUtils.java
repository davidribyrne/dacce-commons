package net.dacce.commons.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils extends org.apache.commons.io.FileUtils
{
	final static Logger logger = LoggerFactory.getLogger(FileUtils.class);




	/**
	 * Adds quotes if needed
	 * 
	 * @param file
	 * @return
	 */
	public static String formatFileName(String file)
	{
		if (file.matches("[ \t]"))
		{
			return "\"" + file + "\"";
		}
		return file;
	}

	
	public static List<String> readConfigFileLines(URL url) throws IOException
	{
		InputStream is = url.openStream();
		return readConfigFileLines(is);
	}
	
	
	public static List<String> readConfigFileLines(String file) throws FileNotFoundException, IOException
	{
		File f = new File(file);
		return readConfigFileLines(f);
	}
	
	public static List<String> readConfigFileLines(File file) throws FileNotFoundException, IOException
	{
		InputStream is = new FileInputStream(file);
		return readConfigFileLines(is);
	}
	
	/**
	 * Ignores empty lines and lines that start with octothorpe (#) or semicolon (;)
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static List<String> readConfigFileLines(InputStream is) throws IOException
	{
		List<String> lines = new ArrayList<String>();
		for(String l: IOUtils.readLines(is))
		{
			String line = l.trim();
			if (l.isEmpty() || l.startsWith("#") || l.startsWith(";"))
				continue;
			lines.add(line);
		}
		is.close();
		return lines;
	}

	/**
	 * Searches the PATH environment variable for the specified executable name. In Windows,
	 * the extension (usually .exe) must be included.
	 *
	 * @param name
	 *            the name
	 * @return a File object pointing at the executable, or null if it wasn't found
	 */
	public static File whichFile(String name)
	{
		for (String path : System.getenv("PATH").split(File.pathSeparator))
		{
			File file = new File(path, name);
			if (file.exists())
			{
				return file;
			}
		}
		return null;
	}


	/**
	 * Searches the PATH environment variable for the specified executable name. In Windows,
	 * the extension (usually .exe) must be included.
	 *
	 * @param name
	 *            the name
	 * @return a File object pointing at the executable, or null if it wasn't found
	 */
	public static String which(String name)
	{
		File file = whichFile(name);
		if (file != null)
		{
			try
			{
				return file.getCanonicalPath();
			}
			catch (IOException e)
			{
				logger.error("Failed to get canonical path for " + file.getPath() + ": " + e.getLocalizedMessage());
			}
		}
		return null;
	}


	public static boolean exists(String filepath)
	{
		File file = new File(filepath);
		return file.exists();
	}


	/**
	 * Creates the directory if it doesn't exist
	 *
	 * @param file
	 *            the file
	 * @throws IllegalArgumentException
	 *             thrown if the name exists, but isn't a directory
	 */
	public static void createDirectory(File file) throws IllegalArgumentException
	{
		if (!file.exists())
		{
			file.mkdirs();
		}
		else if (!file.isDirectory())
		{
			throw new IllegalArgumentException(file.getAbsolutePath() + " exists, but isn't a directory");
		}
	}


	public static void writeToFile(String fileName, String string) throws FileNotFoundException, IOException
	{
		writeToFile(fileName, string.getBytes());
	}


	public static void writeToFile(String fileName, byte[] bytes) throws FileNotFoundException, IOException
	{
		OutputStream outfile = new FileOutputStream(fileName);
		outfile.write(bytes);
		outfile.close();
	}

	private static final String BAD_FILENAME_CHARACTERS = "[^0-9a-zA-Z\\-_\\.]+";


	public static String escapeFilename(String filename)
	{
		return filename.replaceAll(BAD_FILENAME_CHARACTERS, "-");
	}


	public static boolean legalFilename(String filename)
	{
		return !filename.matches(BAD_FILENAME_CHARACTERS);
	}
	
	public static String readFileToString(String path) throws IOException
	{
		return readFileToString(new File(path));
	}
	
	
	public static List<String> readLines(String path) throws IOException
	{
		return readLines(new File(path));
	}
}
