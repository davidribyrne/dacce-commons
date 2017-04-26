package net.dacce.commons.simpleServer;

import java.io.IOException;
import java.net.Socket;


public interface Worker
{
	public void handleSocket(Socket clientSocket) throws IOException;
}
