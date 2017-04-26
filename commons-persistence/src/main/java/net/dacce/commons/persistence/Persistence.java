<<<<<<< Updated upstream
package net.dacce.commons.persistence;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import java.util.*;

import javax.sql.DataSource;

public class Persistence
{
	private final static Logger logger = LoggerFactory.getLogger(Persistence.class);
	private DataSource dataSource;
	
	public Persistence(DataSource dataSource)
	{
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(PersonMapper.class);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		SqlSession session = sqlSessionFactory.openSession();
//		Person person = new Person("david", 40, 1);
		PersonMapper pm = session.getMapper(PersonMapper.class);
//		pm.insertPerson(person);
//		session.commit();
		Person p2 = pm.getPersonByID(1);
		System.out.println(p2.getName());
	}

}
=======
package net.dacce.commons.persistence;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import java.util.*;

import javax.sql.DataSource;

public class Persistence
{
	private final static Logger logger = LoggerFactory.getLogger(Persistence.class);
	private DataSource dataSource;
	
	public Persistence(DataSource dataSource)
	{
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(PersonMapper.class);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		SqlSession session = sqlSessionFactory.openSession();
//		Person person = new Person("david", 40, 1);
		PersonMapper pm = session.getMapper(PersonMapper.class);
//		pm.insertPerson(person);
//		session.commit();
		Person p2 = pm.getPersonByID(1);
		System.out.println(p2.getName());
	}

}
>>>>>>> Stashed changes
