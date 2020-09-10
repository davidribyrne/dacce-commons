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


// TODO: Auto-generated Javadoc
/**
 * The Class NodeDatabaseStorage.
 */
public class NodeDatabaseStorage
{
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(NodeDatabaseStorage.class);


	/** The Constant DRIVER. */
	private final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	
	/** The db connection. */
	private java.sql.Connection dbConnection;
	
	/** The Constant MAX_VALUE_LENGTH. */
	private final static int MAX_VALUE_LENGTH = 65536;
	
	/** The database. */
	private final NodeDatabase database;

	/**
	 * Instantiates a new node database storage.
	 *
	 * @param database the database
	 */
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


	/**
	 * Load or create.
	 *
	 * @param filepath the filepath
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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


	/**
	 * Load types.
	 *
	 * @param database the database
	 * @throws SQLException the SQL exception
	 */
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


	/**
	 * Creates the db.
	 *
	 * @param path the path
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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


	/**
	 * Configure connection.
	 *
	 * @throws SQLException the SQL exception
	 */
	private void configureConnection() throws SQLException
	{
		dbConnection.setAutoCommit(true);
	}


	/**
	 * Shutdown.
	 *
	 * @throws SQLException the SQL exception
	 */
	public void shutdown() throws SQLException
	{
		DriverManager.getConnection("jdbc:derby:;shutdown=true");
	}


	/**
	 * Adds the node.
	 *
	 * @param node the node
	 */
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


	/**
	 * Adds the connection.
	 *
	 * @param connectionId the connection id
	 * @param nodes the nodes
	 * @throws UnexpectedException the unexpected exception
	 */
	void addConnection(UUID connectionId, Node... nodes) throws UnexpectedException
	{
		for (Node node : nodes)
		{
			updateConnection(connectionId, node);
		}
	}


	/**
	 * Update connection.
	 *
	 * @param connectionId the connection id
	 * @param node the node
	 * @throws UnexpectedException the unexpected exception
	 */
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


	/**
	 * Node exists.
	 *
	 * @param id the id
	 * @return boolean
	 * @throws UnexpectedException the unexpected exception
	 * @throws IllegalStateException the illegal state exception
	 */
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


	/**
	 * Gets the node.
	 *
	 * @param id the id
	 * @return the node
	 * @throws SQLException the SQL exception
	 * @throws IllegalStateException the illegal state exception
	 */
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

	/**
	 * Clob to string.
	 *
	 * @param clob the clob
	 * @return the string
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static String clobToString(Clob clob) throws SQLException, IOException
	{
		InputStream in = clob.getAsciiStream();
		StringWriter w = new StringWriter();
		IOUtils.copy(in, w);
		in.close();
		return w.toString();
	}

	/**
	 * Gets the node connections.
	 *
	 * @param node the node
	 * @throws SQLException the SQL exception
	 */
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


	/**
	 * Gets the connection.
	 *
	 * @param uuid the uuid
	 * @return the connection
	 * @throws SQLException the SQL exception
	 */
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


	/**
	 * Adds the node type.
	 *
	 * @param nodeType the node type
	 */
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


	/**
	 * Adds the UUID.
	 *
	 * @param ps the ps
	 * @param position the position
	 * @param uuid the uuid
	 * @throws SQLException the SQL exception
	 */
	private static void addUUID(PreparedStatement ps, int position, UUID uuid) throws SQLException
	{
		addBinary(ps, position, uuidToBytes(uuid));
	}


	/**
	 * Adds the binary.
	 *
	 * @param ps the ps
	 * @param position the position
	 * @param data the data
	 * @throws SQLException the SQL exception
	 */
	private static void addBinary(PreparedStatement ps, int position, byte[] data) throws SQLException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ps.setBinaryStream(position, bais);
	}


	/**
	 * Read UUID.
	 *
	 * @param rs the rs
	 * @param position the position
	 * @return the uuid
	 * @throws SQLException the SQL exception
	 * @throws UnexpectedException the unexpected exception
	 */
	private static UUID readUUID(ResultSet rs, int position) throws SQLException, UnexpectedException
	{
		return bytesToUUID(readBinary(rs, position));
	}


	/**
	 * Read binary.
	 *
	 * @param rs the rs
	 * @param position the position
	 * @return the byte[]
	 * @throws SQLException the SQL exception
	 * @throws UnexpectedException the unexpected exception
	 */
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


	/**
	 * Uuid to bytes.
	 *
	 * @param uuid the uuid
	 * @return the byte[]
	 */
	private static byte[] uuidToBytes(UUID uuid)
	{
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.putLong(uuid.getMostSignificantBits());
		buffer.putLong(uuid.getLeastSignificantBits());
		return buffer.array();
	}


	/**
	 * Bytes to UUID.
	 *
	 * @param bytes the bytes
	 * @return the uuid
	 */
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
