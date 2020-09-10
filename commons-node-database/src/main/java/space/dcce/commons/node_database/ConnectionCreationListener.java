package space.dcce.commons.node_database;


// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving connectionCreation events.
 * The class that is interested in processing a connectionCreation
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addConnectionCreationListener</code> method. When
 * the connectionCreation event occurs, that object's appropriate
 * method is invoked.
 *
 */
public interface ConnectionCreationListener
{
	
	/**
	 * Invoked when connection is created.
	 *
	 * @param connection the connection
	 */
	public void connectionCreated(Connection connection);
}
