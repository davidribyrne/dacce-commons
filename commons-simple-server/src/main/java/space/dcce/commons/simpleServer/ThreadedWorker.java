package space.dcce.commons.simpleServer;

import java.io.IOException;
import java.net.Socket;


// TODO: Auto-generated Javadoc
/**
 * The Class ThreadedWorker.
 */
public abstract class ThreadedWorker implements Runnable, Worker
{
	
	/** The thread. */
	private Thread thread;
	
	/** The client socket. */
	protected Socket clientSocket;


	/**
	 * Instantiates a new threaded worker.
	 */
	public ThreadedWorker()
	{
	}


	/**
	 * Handle socket.
	 *
	 * @param clientSocket the client socket
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void handleSocket(Socket clientSocket) throws IOException
	{
		this.clientSocket = clientSocket;
		thread = new Thread(Thread.currentThread().getThreadGroup(), this, "Server client handler");
		thread.start();
	}
}
