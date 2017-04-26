
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientStub
{
	private final static Logger logger = LoggerFactory.getLogger(ClientStub.class);


	public ClientStub() throws Exception
	{
		Socket clientSocket = new Socket(InetAddress.getLocalHost(), 2063);
		ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		byte b[] = new byte[] { 1, 2, 3, 4 };
		outToServer.writeObject(b);
		clientSocket.close();

	}


	public static void main(String[] args) throws Exception
	{
		new ClientStub();
	}
}
