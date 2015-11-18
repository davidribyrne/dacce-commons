package net.dacce.commons.dns.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.dacce.commons.general.FileUtils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Based on regdom4j
 * https://github.com/hamano/regdom4j/
 *
 */
public class PublicSuffixList
{
	private final static Logger logger = LoggerFactory.getLogger(PublicSuffixList.class);
	private final static String DEFAULT_PATH = "/public_suffix_list.dat";
	private final static PublicSuffixList instance = new PublicSuffixList();

	private boolean loaded;
	private Map<String, Map<String, ?>> tree;


	public PublicSuffixList(String path) throws IOException
	{
		loadList(path, false);
		loaded = true;
	}


	private PublicSuffixList()
	{

	}


	public static PublicSuffixList getDefaultList() throws IOException
	{
		synchronized (instance)
		{
			if (!instance.loaded)
			{
				instance.loadList(DEFAULT_PATH, true);
				instance.loaded = true;
			}
		}
		return instance;
	}


	private void loadList(String path, boolean resource) throws IOException
	{
		List<String> lines;
		if (resource)
		{
			URL url = getClass().getResource(path);
			InputStream is = url.openStream();
			lines = IOUtils.readLines(is);
			is.close();
		}
		else
		{
			lines = FileUtils.readLines(path);
		}
		tree = new HashMap<String, Map<String, ?>>();


		for (String l : lines)
		{
			String line = l.trim();
			if (line.startsWith("//") || line.isEmpty())
			{
				continue;
			}
			List<String> list = Arrays.asList(line.split("\\."));
			LinkedList<String> parts = new LinkedList<String>(list);
			buildSubdomain(tree, parts);
		}

	}


	public String getRegisteredDomain(String fqdn)
	{
		List<String> list = Arrays.asList(fqdn.split("\\."));
		LinkedList<String> parts = new LinkedList<String>(list);
		String result = findRegisteredDomain(parts, tree);
		return result;
	}


	@SuppressWarnings("unchecked")
	private String findRegisteredDomain(LinkedList<String> parts,
			Map<String, Map<String, ?>> node)
	{
		if (parts.size() == 0)
		{
			return null;
		}
		String sub = parts.removeLast();
		String result = null;
		if (node.get("!") != null)
		{
			return "";
		}
		else if (node.get(sub) != null)
		{
			result = findRegisteredDomain(parts, (Map<String, Map<String, ?>>) node.get(sub));
		}
		else if (node.get("*") != null)
		{
			result = findRegisteredDomain(parts, (Map<String, Map<String, ?>>) node.get("*"));
		}
		else
		{
			return sub;
		}
		if (result == null)
		{
			return null;
		}
		else if (result.equals(""))
		{
			return sub;
		}
		else
		{
			return result + "." + sub;
		}
	}


	private void buildSubdomain(Map<String, Map<String, ?>> node,
			LinkedList<String> parts)
	{
		boolean isNotDomain = false;
		String sub = parts.removeLast();
		if (sub.startsWith("!"))
		{
			isNotDomain = true;
			sub = sub.substring(1);
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Map<String, ?>> child = (Map<String, Map<String, ?>>) node.get(sub);
		if (child == null)
		{
			child = new HashMap<String, Map<String, ?>>();
			if (isNotDomain)
			{
				child.put("!", new HashMap<String, Map<String, ?>>(0));
			}
			node.put(sub, child);
		}
		if (!isNotDomain && parts.size() > 0)
		{
			buildSubdomain(child, parts);
		}
	}

}
