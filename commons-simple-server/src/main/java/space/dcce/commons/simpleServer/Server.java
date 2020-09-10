package space.dcce.commons.simpleServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// TODO: Auto-generated Javadoc
/**
 * The Class Server.
 */
public abstract class Server implements Runnable
{
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(Server.class);

	/** The host address. */
	private InetAddress hostAddress;
	
	/** The port. */
	private int port;
	
	/** The server socket. */
	private ServerSocket serverSocket;
	
	/** The server thread. */
	private Thread serverThread;
	
	/** The running. */
	private boolean running;
	
	/** The thread group. */
	private ThreadGroup threadGroup;


	/**
	 * Instantiates a new server.
	 *
	 * @param hostAddress the host address
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Server(InetAddress hostAddress, int port) throws IOException
	{
		this.hostAddress = hostAddress;
		this.port = port;

		threadGroup = new ThreadGroup("Simple server");

		serverSocket = new ServerSocket(port, 50, hostAddress);
		running = true;
		serverThread = new Thread(threadGroup, this, "Simple server accept thread");
		serverThread.start();
	}


	/**
	 * Checks if is running.
	 *
	 * @return true, if is running
	 */
	public boolean isRunning()
	{
		return running;
	}


	/**
	 * Creates the worker.
	 *
	 * @return the worker
	 */
	protected abstract Worker createWorker();


	/**
	 * Run.
	 */
	@Override
	public void run()
	{
		try
		{
			while (isRunning())
			{
				try
				{
					Socket clientSocket;

					synchronized (serverSocket)
					{
						clientSocket = serverSocket.accept();
					}
					Worker worker = createWorker();
					worker.handleSocket(clientSocket);
				}
				catch (IOException e)
				{
					logger.error("Problem with simple socket server: " + e.getLocalizedMessage(), e);
				}
			}
		}
		catch (Throwable t)
		{
			logger.error("Uncaught exception in Server.run: " + t.getLocalizedMessage(), t);
		}
	}


	/**
	 * Sets the running.
	 *
	 * @param running the new running
	 */
	public void setRunning(boolean running)
	{
		this.running = running;
	}


}
