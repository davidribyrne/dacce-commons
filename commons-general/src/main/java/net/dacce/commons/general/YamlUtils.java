package net.dacce.commons.general;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

public class YamlUtils
{

	private YamlUtils()
	{
	}
	
	public static Object readObject(String filename, Class<?> clazz) throws FileNotFoundException, YamlException
	{
		YamlReader reader = new YamlReader(new FileReader(filename));
		return reader.read(clazz);
	}
	
	public static void writeObject(String filename, Object object) throws YamlException, IOException
	{
		YamlWriter writer = new YamlWriter(new FileWriter(filename));
		writer.write(object);
	}
}
