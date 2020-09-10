package space.dcce.commons.general;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

// TODO: Auto-generated Javadoc
/**
 * The Class YamlUtils.
 */
public class YamlUtils
{

	/**
	 * Instantiates a new yaml utils.
	 */
	private YamlUtils()
	{
	}
	
	/**
	 * Read object.
	 *
	 * @param filename the filename
	 * @param clazz the clazz
	 * @return the object
	 * @throws FileNotFoundException the file not found exception
	 * @throws YamlException the yaml exception
	 */
	public static Object readObject(String filename, Class<?> clazz) throws FileNotFoundException, YamlException
	{
		YamlReader reader = new YamlReader(new FileReader(filename));
		return reader.read(clazz);
	}
	
	/**
	 * Write object.
	 *
	 * @param filename the filename
	 * @param object the object
	 * @throws YamlException the yaml exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeObject(String filename, Object object) throws YamlException, IOException
	{
		YamlWriter writer = new YamlWriter(new FileWriter(filename));
		writer.write(object);
	}
}
