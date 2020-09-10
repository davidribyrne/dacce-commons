package space.dcce.commons.persistence;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;


// TODO: Auto-generated Javadoc
/**
 * The Class Test.
 */
public class Test
{

	/**
	 * Instantiates a new test.
	 */
	public Test()
	{
	}


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception
	{
		Class.forName("org.sqlite.JDBC");
		Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
		SQLiteConfig config = new SQLiteConfig();
		SQLiteDataSource dataSource = new SQLiteDataSource(config);
		dataSource.setUrl("jdbc:sqlite:database.db");
		
		ScriptRunner sr = new ScriptRunner(connection);
		sr.setEscapeProcessing(false);
		sr.runScript(new FileReader(Test.class.getClassLoader().getResource("schema.sql").getFile()));
		new Persistence(dataSource);
		
	}
}
