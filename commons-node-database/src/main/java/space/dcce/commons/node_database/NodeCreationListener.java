package space.dcce.commons.node_database;


// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving nodeCreation events.
 * The class that is interested in processing a nodeCreation
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addNodeCreationListener</code> method. When
 * the nodeCreation event occurs, that object's appropriate
 * method is invoked.
 *
 */
public interface NodeCreationListener
{
	
	/**
	 * Invoked when node is created.
	 *
	 * @param node the node
	 */
	public void nodeCreated(Node node);
}
