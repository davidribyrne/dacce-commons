package space.dcce.commons.simpleServer;

import java.io.IOException;
import java.net.Socket;


// TODO: Auto-generated Javadoc
/**
 * The Interface Worker.
 */
public interface Worker
{
	
	/**
	 * Handle socket.
	 *
	 * @param clientSocket the client socket
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void handleSocket(Socket clientSocket) throws IOException;
}
