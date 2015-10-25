package net.dacce.commons.general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileUtils
{
	final static Logger logger = LoggerFactory.getLogger(FileUtils.class);


	public static List<String> getFileAsLines(String filePath) throws FileNotFoundException, IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		ArrayList<String> lines = new ArrayList<String>();
		while ((line = reader.readLine()) != null)
		{
			lines.add(line);
		}
		reader.close();

		return lines;
	}


	public static String slurp(String filePath) throws FileNotFoundException, IOException
	{
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}

	
	/**
	 * Adds quotes if needed
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

}
