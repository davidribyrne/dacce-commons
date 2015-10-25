package net.dacce.commons.dns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.*;


public class SystemDnsServers
{
	private final static Logger logger = LoggerFactory.getLogger(SystemDnsServers.class);


	static public List<String> getSystemDnsServers()
	{
		List<String> servers;

		servers = getJvmDnsServers();
		if (!servers.isEmpty())
			return servers;

		String OS = System.getProperty("os.name");
		String vendor = System.getProperty("java.vendor");
		if (OS.indexOf("Windows") != -1)
		{
			if (OS.indexOf("95") != -1 ||
					OS.indexOf("98") != -1 ||
					OS.indexOf("ME") != -1)
				find95();
			else
				findNT();
		}
		else if (OS.indexOf("NetWare") != -1)
		{
			findNetware();
		}
		else if (vendor.indexOf("Android") != -1)
		{
			findAndroid();
		}
		else
		{
			findUnix();
		}


		return null;
	}


	private static void
			find95()
	{
		String s = "winipcfg.out";
		try
		{
			Process p;
			p = Runtime.getRuntime().exec("winipcfg /all /batch " + s);
			p.waitFor();
			File f = new File(s);
			findWin(new FileInputStream(f));
			new File(s).delete();
		}
		catch (Exception e)
		{
			return;
		}
	}


	/**
	 * Calls ipconfig and parses the result to find servers and a search path.
	 */
	private void
			findNT()
	{
		try
		{
			Process p;
			p = Runtime.getRuntime().exec("ipconfig /all");
			findWin(p.getInputStream());
			p.destroy();
		}
		catch (Exception e)
		{
			return;
		}
	}


	/**
	 * Parses the output of getprop, which is the only way to get DNS
	 * info on Android. getprop might disappear in future releases, so
	 * this code comes with a use-by date.
	 */
	private void
			findAndroid()
	{
		// This originally looked for all lines containing .dns; but
		// http://code.google.com/p/android/issues/detail?id=2207#c73
		// indicates that net.dns* should always be the active nameservers, so
		// we use those.
		final String re1 = "^\\d+(\\.\\d+){3}$";
		final String re2 = "^[0-9a-f]+(:[0-9a-f]*)+:[0-9a-f]+$";
		ArrayList lserver = new ArrayList();
		ArrayList lsearch = new ArrayList();
		try
		{
			Class SystemProperties =
					Class.forName("android.os.SystemProperties");
			Method method =
					SystemProperties.getMethod("get",
							new Class[] { String.class });
			final String[] netdns = new String[] { "net.dns1", "net.dns2",
					"net.dns3", "net.dns4" };
			for (int i = 0; i < netdns.length; i++)
			{
				Object[] args = new Object[] { netdns[i] };
				String v = (String) method.invoke(null, args);
				if (v != null &&
						(v.matches(re1) || v.matches(re2)) &&
						!lserver.contains(v))
					lserver.add(v);
			}
		}
		catch (Exception e)
		{
			// ignore resolutely
		}
		configureFromLists(lserver, lsearch);
	}


	private void
			findWin(InputStream in, Locale locale)
	{
		String packageName = ResolverConfig.class.getPackage().getName();
		String resPackageName = packageName + ".windows.DNSServer";
		ResourceBundle res;
		if (locale != null)
			res = ResourceBundle.getBundle(resPackageName, locale);
		else
			res = ResourceBundle.getBundle(resPackageName);

		String host_name = res.getString("host_name");
		String primary_dns_suffix = res.getString("primary_dns_suffix");
		String dns_suffix = res.getString("dns_suffix");
		String dns_servers = res.getString("dns_servers");

		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try
		{
			List lserver = new ArrayList();
			List lsearch = new ArrayList();
			String line = null;
			boolean readingServers = false;
			boolean readingSearches = false;
			while ((line = br.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line);
				if (!st.hasMoreTokens())
				{
					readingServers = false;
					readingSearches = false;
					continue;
				}
				String s = st.nextToken();
				if (line.indexOf(":") != -1)
				{
					readingServers = false;
					readingSearches = false;
				}

				if (line.indexOf(host_name) != -1)
				{
					while (st.hasMoreTokens())
						s = st.nextToken();
					Name name;
					try
					{
						name = Name.fromString(s, null);
					}
					catch (TextParseException e)
					{
						continue;
					}
					if (name.labels() == 1)
						continue;
					addSearch(s, lsearch);
				}
				else if (line.indexOf(primary_dns_suffix) != -1)
				{
					while (st.hasMoreTokens())
						s = st.nextToken();
					if (s.equals(":"))
						continue;
					addSearch(s, lsearch);
					readingSearches = true;
				}
				else if (readingSearches ||
						line.indexOf(dns_suffix) != -1)
				{
					while (st.hasMoreTokens())
						s = st.nextToken();
					if (s.equals(":"))
						continue;
					addSearch(s, lsearch);
					readingSearches = true;
				}
				else if (readingServers ||
						line.indexOf(dns_servers) != -1)
				{
					while (st.hasMoreTokens())
						s = st.nextToken();
					if (s.equals(":"))
						continue;
					addServer(s, lserver);
					readingServers = true;
				}
			}

			configureFromLists(lserver, lsearch);
		}
		catch (IOException e)
		{
		}
		return;
	}


	/**
	 * Looks in /etc/resolv.conf to find servers and a search path.
	 * "nameserver" lines specify servers. "domain" and "search" lines
	 * define the search path.
	 */
	private void
			findResolvConf(String file)
	{
		InputStream in = null;
		try
		{
			in = new FileInputStream(file);
		}
		catch (FileNotFoundException e)
		{
			return;
		}
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		List lserver = new ArrayList(0);
		List lsearch = new ArrayList(0);
		int lndots = -1;
		try
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				if (line.startsWith("nameserver"))
				{
					StringTokenizer st = new StringTokenizer(line);
					st.nextToken(); /* skip nameserver */
					addServer(st.nextToken(), lserver);
				}
				else if (line.startsWith("domain"))
				{
					StringTokenizer st = new StringTokenizer(line);
					st.nextToken(); /* skip domain */
					if (!st.hasMoreTokens())
						continue;
					if (lsearch.isEmpty())
						addSearch(st.nextToken(), lsearch);
				}
				else if (line.startsWith("search"))
				{
					if (!lsearch.isEmpty())
						lsearch.clear();
					StringTokenizer st = new StringTokenizer(line);
					st.nextToken(); /* skip search */
					while (st.hasMoreTokens())
						addSearch(st.nextToken(), lsearch);
				}
				else if (line.startsWith("options"))
				{
					StringTokenizer st = new StringTokenizer(line);
					st.nextToken(); /* skip options */
					while (st.hasMoreTokens())
					{
						String token = st.nextToken();
						if (token.startsWith("ndots:"))
						{
							lndots = parseNdots(token);
						}
					}
				}
			}
			br.close();
		}
		catch (IOException e)
		{
		}

		configureFromLists(lserver, lsearch);
		configureNdots(lndots);
	}


	private void
			findUnix()
	{
		findResolvConf("/etc/resolv.conf");
	}


	private void
			findNetware()
	{
		findResolvConf("sys:/etc/resolv.cfg");
	}


	// Derived from dnsjava
	private static List<String> getJvmDnsServers()
	{
		List<String> servers = new ArrayList<String>(1);

		try
		{
			Class<?> resConfClass = Class.forName("sun.net.dns.ResolverConfiguration");


			Object resConf;
			Class<?>[] noClasses = new Class[0];
			Object[] noObjects = new Object[0];

			// ResolverConfiguration resConf = ResolverConfiguration.open();
			Method open = resConfClass.getDeclaredMethod("open", noClasses);
			resConf = open.invoke(null, noObjects);

			// lserver_tmp = resConf.nameservers();
			Method nameservers = resConfClass.getMethod("nameservers",
					noClasses);
			List<String> s = (List<String>) nameservers.invoke(resConf, noObjects);
			if (s != null && !s.isEmpty())
				servers.addAll(s);
		}
		catch (Exception e)
		{
			logger.debug("Failed to get DNS configuration from JVM: " + e.getLocalizedMessage(), e);
		}
		return servers;
	}


private void
findWin(InputStream in) {
	String property = "org.xbill.DNS.windows.parse.buffer";
	final int defaultBufSize = 8 * 1024;
	int bufSize = Integer.getInteger(property, defaultBufSize).intValue();
	BufferedInputStream b = new BufferedInputStream(in, bufSize);
	b.mark(bufSize);
	findWin(b, null);
	if (servers == null) {
		try {
			b.reset();
		} 
		catch (IOException e) {
			return;
		}
		findWin(b, new Locale("", ""));
	}
}

	private SystemDnsServers()
	{
	}
}
