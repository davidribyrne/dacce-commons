package space.dcce.commons.simpleServer;

import java.io.IOException;
import java.net.Socket;


public abstract class ThreadedWorker implements Runnable, Worker
{
	private Thread thread;
	protected Socket clientSocket;


	public ThreadedWorker()
	{
	}


	@Override
	public void handleSocket(Socket clientSocket) throws IOException
	{
		this.clientSocket = clientSocket;
		thread = new Thread(Thread.currentThread().getThreadGroup(), this, "Server client handler");
		thread.start();
	}
}
