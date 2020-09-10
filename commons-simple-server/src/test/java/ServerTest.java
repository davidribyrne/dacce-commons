
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import space.dcce.commons.simpleServer.Server;
import space.dcce.commons.simpleServer.ThreadedWorker;
import space.dcce.commons.simpleServer.Worker;


// TODO: Auto-generated Javadoc
/**
 * The Class ServerTest.
 */
public class ServerTest extends Server
{
	
	/**
	 * Instantiates a new server test.
	 *
	 * @param hostAddress the host address
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ServerTest(InetAddress hostAddress, int port) throws IOException
	{
		super(hostAddress, port);
	}


	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(ServerTest.class);


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws UnknownHostException the unknown host exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		new ServerTest(InetAddress.getLoopbackAddress(), 2063);
	}


	/**
	 * Creates the worker.
	 *
	 * @return the worker
	 */
	@Override
	protected Worker createWorker()
	{
		return new ThreadedWorker()
		{

			@Override
			public void run()
			{
				while(true)
				{
					try
					{
						ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());
						Object o = is.readObject();
						System.out.println(o.toString());
					}
					catch (IOException e)
					{
						logger.error("Problem with simple socket server worker: " + e.getLocalizedMessage(), e);
					}
					catch (ClassNotFoundException e)
					{
						logger.error("Unknown class: " + e.getLocalizedMessage(), e);
					}
				}
			}
		};
	}
}
