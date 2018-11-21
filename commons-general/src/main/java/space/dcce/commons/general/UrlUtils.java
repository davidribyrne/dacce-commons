package space.dcce.commons.general;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils
{


	private UrlUtils()
	{
	}
	
	/**
	 * Returns simplified base URL
	 * @param url
	 * @return
	 */
	public static URL getBaseUrl(URL url)
	{
		try
		{
			if (url.getPort() != url.getDefaultPort())
				return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getPath());
			else
				return new URL(url.getProtocol(), url.getHost(), url.getPath());
		}
		catch (MalformedURLException e)
		{
			throw new UnexpectedException(e);
		}
	}
}
