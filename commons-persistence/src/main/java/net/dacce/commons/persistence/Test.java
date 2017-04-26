<<<<<<< Updated upstream
package net.dacce.commons.persistence;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;


public class Test
{

	public Test()
	{
	}


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
		Persistence p = new Persistence(dataSource);

		
	}
}
=======
package net.dacce.commons.persistence;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;


public class Test
{

	public Test()
	{
	}


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
		Persistence p = new Persistence(dataSource);

		
	}
}
>>>>>>> Stashed changes
