package net.dacce.commons.dns.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainUtils
{
	private final static Logger logger = LoggerFactory.getLogger(DomainUtils.class);


	/**
	 * Returns the hostname's domain
	 * @param hostname
	 * @return
	 */
	public static String getDomain(String hostname)
	{
		try
		{
			return PublicSuffixList.getDefaultList().getRegisteredDomain(hostname);
		}
		catch (IOException e)
		{
			logger.error("Failed to load default public suffix list: " + e.getLocalizedMessage(), e);
			return null;
		}
	}
	
	/**
	 * This will NOT return the domain. If a domain is passed as the hostname, an empty list
	 * is returned.
	 * @param hostname
	 * @return
	 */
	public static List<String> getSubdomains(String hostname)
	{
		List<String> subdomains = new ArrayList<String>();
		String domain = getDomain(hostname);
		if (domain.isEmpty())
		{
			logger.error("Failed to get domain for " + hostname);
		}
		else
		{
			String stripped = hostname.replaceAll("\\.?" + Pattern.quote(domain) + "$", "");
			if (!stripped.isEmpty())
			{
				String[] components = stripped.split("\\.");
				String subdomain = domain;
				for (int i = components.length - 1; i >= 0; i--)
				{
					subdomain = components[i] + domain;
					subdomains.add(subdomain);
				}
			}
		}
		return subdomains;
	}
	
	private DomainUtils()
	{
	}
}
