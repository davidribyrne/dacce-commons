package space.dcce.commons.general;

import java.net.MalformedURLException;
import java.net.URL;

// TODO: Auto-generated Javadoc
/**
 * The Class UrlUtils.
 */
public class UrlUtils
{


	/**
	 * Instantiates a new url utils.
	 */
	private UrlUtils()
	{
	}
	
	/**
	 * Returns simplified base URL.
	 *
	 * @param url the url
	 * @return the base url
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
