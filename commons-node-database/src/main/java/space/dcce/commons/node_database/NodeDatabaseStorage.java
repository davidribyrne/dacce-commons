package space.dcce.commons.node_database;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.file.FileAlreadyExistsException;
import java.sql.Clob;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.dcce.commons.general.FileUtils;
import space.dcce.commons.general.UnexpectedException;


public class NodeDatabaseStorage
{
	private final static Logger logger = LoggerFactory.getLogger(NodeDatabaseStorage.class);


	private final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private java.sql.Connection dbConnection;
	private String filename;
	private final static int MAX_VALUE_LENGTH = 65536;
	private final NodeDatabase database;

	// I know that passing in database is bad. I'll fix it later.
	public NodeDatabaseStorage(NodeDatabase database)
	{
		this.database = database;
		try
		{
			Class.forName(DRIVER).newInstance();
		}
		catch (InstantiationException e)
		{
			throw new UnexpectedException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new UnexpectedException(e);
		}
		catch (ClassNotFoundException e)
		{
			System.err.println(e.getLocalizedMessage());
			System.err.println(e.toString());
			System.err.println("You need to have a driver for the database.");
			System.exit(1);
		}
	}


	public void loadOrCreate(String filepath) throws SQLException, IOException
	{
//		String file = path + File.separator + filename;
		if (!FileUtils.exists(filepath))
		{
			createDb(filepath);
		}
		else
		{
			dbConnection = DriverManager.getConnection("jdbc:derby:" + filepath + ";create=false");
			configureConnection();
		}
		loadTypes(database);
	}


	private void loadTypes(NodeDatabase database) throws SQLException
	{
		String query = "SELECT id, name, description FROM types";

		ResultSet rs = dbConnection.prepareStatement(query).executeQuery();
		while (rs.next())
		{
			UUID uuid = readUUID(rs, 1);
			String name = rs.getString(2);
			String description = rs.getString(3);
			new NodeType(database, uuid, name, description, true);
		}

	}


	public void createDb(String path) throws SQLException, IOException
	{
		if (FileUtils.exists(path))
		{
			throw new FileAlreadyExistsException("There is already a file at '" + path + "'");
		}

		dbConnection = DriverManager.getConnection("jdbc:derby:" + path + ";create=true");
		configureConnection();

		File schemaFile = new File(this.getClass().getClassLoader().getResource("schema.sql").getFile());
		Statement statement = dbConnection.createStatement();
		for (String command : FileUtils.readFileToString(schemaFile).split("//////////"))
		{
			if (command.matches("^\\s*$"))
				continue;

			statement.execute(command);
		}

		statement.close();

	}


	private void configureConnection() throws SQLException
	{
		dbConnection.setAutoCommit(true);
	}


	public void shutdown() throws SQLException
	{
		DriverManager.getConnection("jdbc:derby:;shutdown=true");
	}


	void addNode(Node node)
	{
		try
		{
			PreparedStatement psInsert = dbConnection.prepareStatement(
					"INSERT INTO nodes (id, type, value) VALUES (?, ?, ?)");
			addUUID(psInsert, 1, node.getID());
			addUUID(psInsert, 2, node.getNodeType().getID());
			String value = node.getValue();
			if (value.length() > MAX_VALUE_LENGTH)
			{
				value = value.substring(0, MAX_VALUE_LENGTH);
			}
			// Clob valueClob = dbConnection.createClob();
			// valueClob.setString(1, value);
			// psInsert.setClob(3, valueClob);
			psInsert.setAsciiStream(3, new ByteArrayInputStream(value.getBytes()));
			psInsert.execute();
			psInsert.close();
		}
		catch (SQLException e)
		{
			logger.error("Unexpected problem running SQL for inserting a new node type", e);
			throw new UnexpectedException(e);
		}
	}


	void addConnection(UUID connectionId, Node... nodes) throws UnexpectedException
	{
		for (Node node : nodes)
		{
			updateConnection(connectionId, node);
		}
	}


	void updateConnection(UUID connectionId, Node node) throws UnexpectedException
	{
		try
		{
			PreparedStatement psInsert = dbConnection.prepareStatement(
					"INSERT INTO connections (id, nodeID) VALUES (?, ?)");

			addUUID(psInsert, 1, connectionId);
			addUUID(psInsert, 2, node.getID());
			psInsert.execute();
			psInsert.close();
		}
		catch (SQLException e)
		{
			logger.error("Unexpected problem running SQL for inserting a new node type", e);
			throw new UnexpectedException(e);
		}

	}


	public boolean nodeExists(UUID id) throws UnexpectedException, IllegalStateException
	{
		String query = "SELECT COUNT(id) "
				+ "FROM nodes "
				+ "WHERE "
				+ "id = ? ";

		PreparedStatement statement = null;
		try
		{
			statement = dbConnection.prepareStatement(query);
			addUUID(statement, 1, id);
			ResultSet rs = statement.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			rs.close();

			if (count == 0)
				return false;
			else if (count == 1)
				return true;
			throw new IllegalStateException("More than one node discovered");
		}
		catch (SQLException e)
		{
			throw new UnexpectedException("Unexpected problem running SQL for nodeExists", e);
		}
		finally
		{
			if (statement != null)
			{
				try
				{
					statement.close();
				}
				catch (SQLException e)
				{
					logger.error("Problem closing statement: " + e.getLocalizedMessage(), e);
				}
			}
		}
	}


	public Node getNode(UUID id) throws SQLException, IllegalStateException
	{

		String query = "SELECT value, type "
				+ "FROM nodes "
				+ "WHERE "
				+ "id = ? ";

		PreparedStatement statement = null;
		statement = dbConnection.prepareStatement(query);
		addUUID(statement, 1, id);
		ResultSet rs = statement.executeQuery();
		if (!rs.next())
		{
			return null;
		}

		String value;
		try
		{
			value = clobToString(rs.getClob(1));
		}
		catch (IOException e)
		{
			throw new UnexpectedException(e);
		}
		UUID typeId = readUUID(rs, 2);
		rs.close();
		Node node = new Node(database, database.getByID(typeId), value);

		getNodeConnections(node);
		return node;
	}

	private static String clobToString(Clob clob) throws SQLException, IOException
	{
		InputStream in = clob.getAsciiStream();
		StringWriter w = new StringWriter();
		IOUtils.copy(in, w);
		in.close();
		return w.toString();
	}

	private void getNodeConnections(Node node) throws SQLException
	{
		String query = "SELECT id "
				+ "FROM connections "
				+ "WHERE nodeID = ?";

		PreparedStatement statement = dbConnection.prepareStatement(query);
		addUUID(statement, 1, node.getID());
		ResultSet rs = statement.executeQuery();
		while (rs.next())
		{
			UUID uuid = readUUID(rs, 1);
			Connection c = database.connectionCache.getValue(uuid);
			if (c == null)
			{
				c = getConnection(uuid);
			}
			node.addConnection(c);
		}

	}


	public Connection getConnection(UUID uuid) throws SQLException
	{
		String query = "SELECT nodeID "
				+ "FROM connections "
				+ "WHERE id = ?";

		PreparedStatement statement = dbConnection.prepareStatement(query);
		addUUID(statement, 1, uuid);
		ResultSet rs = statement.executeQuery();
		Connection connection = new Connection(database, uuid);
		while (rs.next())
		{
			UUID nodeID = readUUID(rs, 1);
			connection.restoreNode(nodeID);
		}
		statement.close();
		rs.close();
		return connection;
	}


	void addNodeType(NodeType nodeType)
	{
		try
		{
			PreparedStatement psInsert = dbConnection.prepareStatement(
					"INSERT INTO types (id, name, description) VALUES (?, ?, ?)");

			addUUID(psInsert, 1, nodeType.getID());
			psInsert.setString(2, nodeType.getName());
			psInsert.setString(3, nodeType.getDescription());
			psInsert.execute();
			psInsert.close();
		}
		catch (SQLException e)
		{
			logger.error("Unexpected problem running SQL for inserting a new node type", e);
			throw new UnexpectedException(e);
		}
	}


	private static void addUUID(PreparedStatement ps, int position, UUID uuid) throws SQLException
	{
		addBinary(ps, position, uuidToBytes(uuid));
	}


	private static void addBinary(PreparedStatement ps, int position, byte[] data) throws SQLException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ps.setBinaryStream(position, bais);
	}


	private static UUID readUUID(ResultSet rs, int position) throws SQLException, UnexpectedException
	{
		return bytesToUUID(readBinary(rs, position));
	}


	private static byte[] readBinary(ResultSet rs, int position) throws SQLException, UnexpectedException
	{
		InputStream is = rs.getBinaryStream(position);
		try
		{
			return IOUtils.toByteArray(is);
		}
		catch (IOException e)
		{
			logger.error("Unexpected problem reading SQL result", e);
			throw new UnexpectedException(e);
		}
	}


	private static byte[] uuidToBytes(UUID uuid)
	{
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.putLong(uuid.getMostSignificantBits());
		buffer.putLong(uuid.getLeastSignificantBits());
		return buffer.array();
	}


	private static UUID bytesToUUID(byte[] bytes)
	{
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.put(bytes);
		buffer.flip();
		long mostSignificant = buffer.getLong();
		long leastSignificant = buffer.getLong();
		return new UUID(mostSignificant, leastSignificant);
	}

}
