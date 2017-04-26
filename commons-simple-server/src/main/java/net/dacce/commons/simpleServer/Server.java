package net.dacce.commons.simpleServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class Server implements Runnable
{
	private final static Logger logger = LoggerFactory.getLogger(Server.class);

	private InetAddress hostAddress;
	private int port;
	private ServerSocket serverSocket;
	private Thread serverThread;
	private boolean running;
	private ThreadGroup threadGroup;


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


	public boolean isRunning()
	{
		return running;
	}


	protected abstract Worker createWorker();


	@Override
	public void run()
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


	public void setRunning(boolean running)
	{
		this.running = running;
	}


}
