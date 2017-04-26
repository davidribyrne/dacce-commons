
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dacce.commons.simpleServer.Server;
import net.dacce.commons.simpleServer.ThreadedWorker;
import net.dacce.commons.simpleServer.Worker;


public class ServerTest extends Server
{
	public ServerTest(InetAddress hostAddress, int port) throws IOException
	{
		super(hostAddress, port);
	}


	private final static Logger logger = LoggerFactory.getLogger(ServerTest.class);


	public static void main(String[] args) throws UnknownHostException, IOException
	{
		new ServerTest(InetAddress.getLocalHost(), 2063);
	}


	@Override
	protected Worker createWorker()
	{
		return new ThreadedWorker()
		{

			@Override
			public void run()
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
		};
	}
}
