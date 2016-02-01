package net.dacce.commons.dns.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SystemDnsServers
{
	private final static Logger logger = LoggerFactory.getLogger(SystemDnsServers.class);


	static public List<String> getSystemDnsServers()
	{
		List<String> servers = new ArrayList<String>();

		getJvmDnsServers(servers);
		if (!servers.isEmpty())
			return servers;

		String OS = System.getProperty("os.name");
		String vendor = System.getProperty("java.vendor");
		if (OS.indexOf("Windows") != -1)
		{
			if (OS.indexOf("95") != -1 ||
					OS.indexOf("98") != -1 ||
					OS.indexOf("ME") != -1)
				{
				// Not supported
				}
			else
				findWinNt(servers);
		}
		else if (OS.indexOf("NetWare") != -1)
		{
			findNetware(servers);
		}
		else if (vendor.indexOf("Android") != -1)
		{
			findAndroid(servers);
		}
		else
		{
			findUnix(servers);
		}


		return null;
	}




	/**
	 * Parses the output of getprop, which is the only way to get DNS
	 * info on Android. getprop might disappear in future releases, so
	 * this code comes with a use-by date.
	 */
	private static void findAndroid(List<String> servers)
	{
		// This originally looked for all lines containing .dns; but
		// http://code.google.com/p/android/issues/detail?id=2207#c73
		// indicates that net.dns* should always be the active nameservers, so
		// we use those.
		final String re1 = "^\\d+(\\.\\d+){3}$";
		final String re2 = "^[0-9a-f]+(:[0-9a-f]*)+:[0-9a-f]+$";
		try
		{
			Class<?> SystemProperties = Class.forName("android.os.SystemProperties");
			Method method = SystemProperties.getMethod("get", new Class[] { String.class });
			final String[] netdns = new String[] { "net.dns1", "net.dns2", "net.dns3", "net.dns4" };
			for (int i = 0; i < netdns.length; i++)
			{
				Object[] args = new Object[] { netdns[i] };
				String v = (String) method.invoke(null, args);
				if (v != null &&
						(v.matches(re1) || v.matches(re2)) &&
						!servers.contains(v))
					servers.add(v);
			}
		}
		catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e)
		{
			logger.debug("Failed to discover Android DNS servers: " + e.getLocalizedMessage(), e);
		}
	}




	/**
	 * Looks in /etc/resolv.conf to find servers and a search path.
	 * "nameserver" lines specify servers. "domain" and "search" lines
	 * define the search path.
	 * @throws IOException 
	 */
	private static void findResolvConf(String file, List<String> servers)
	{
		InputStream in = null;
		try
		{
			in = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null)
			{
				if (line.startsWith("nameserver"))
				{
					StringTokenizer st = new StringTokenizer(line);
					st.nextToken(); /* skip nameserver */
					servers.add(st.nextToken());
				}
				else if (line.startsWith("domain"))
				{
				}
				else if (line.startsWith("search"))
				{
				}
				else if (line.startsWith("options"))
				{
				}
			}
			br.close();
		}
		catch (IOException e)
		{
			logger.warn("Error reading DNS servers from " + file + ": " + e.getLocalizedMessage(), e);
		}
	}


	private static void findUnix(List<String> servers) 
	{
		findResolvConf("/etc/resolv.conf", servers);
	}


	private static void findNetware(List<String> servers)
	{
		findResolvConf("sys:/etc/resolv.cfg", servers);
	}


	// Derived from dnsjava
	private static void getJvmDnsServers(List<String> servers)
	{
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
	}

	// English, Polish, Japanese, French, and German
	private static final String dnsLanguages = "(?:DNS Servers|Serwery DNS|DNS \u30b5\u30fc\u30d0\u30fc|Serveurs DNS|DNS-Server)";
	
	private static final String ip4or6Address = 								
			"(?:\\d{1,3}\\.){3}\\d{1,3}" // IPv4 address
			+ "|" // or
			+ "(?:[0-9a-f]{0,4}:){5,7}[0-9a-f]{0,4}(?:%\\d)?"; // IPv6 address
	
	private static void parseWindowsOutput(List<String> servers, String output)
	{
		String pstring = dnsLanguages + "[ \\.]+: "
				+ "(" // Grouping for all DNS addresses for this interface
					+ "(?:" // Grouping of IP address and following whitespace
						+ "(?:" + ip4or6Address + ")\\s+" // end of IP address, followed by whitespace
					+ ")+" // One or more IP address + following whitespace
				+ ")";
		Pattern pattern = Pattern.compile(pstring, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = pattern.matcher(output);
		while (m.find())
		{
			for (int i = 1; i <= m.groupCount(); i++)
			{
				Pattern p2 = Pattern.compile("(" + ip4or6Address + ")");
				Matcher m2 = p2.matcher(m.group(i));
				while(m2.find())
				{
					servers.add(m2.group(1));
				}
			}
		}
	}

	private static void findWinNt(List<String> servers)
	{
		try
		{
			Process p = Runtime.getRuntime().exec("ipconfig /all");
			String output = new String(IOUtils.toByteArray(p.getInputStream()));
			parseWindowsOutput(servers, output);
		}
		catch (IOException e)
		{
			logger.warn("Error obtaining DNS servers from Windows command line: " + e.getLocalizedMessage(), e);
		}
	}

//	private static void find95(List<String> servers)
//	{
//		String s = "winipcfg.out";
//		Process p;
//		p = Runtime.getRuntime().exec("winipcfg /all /batch " + s);
//		p.waitFor();
//		FileUtils.slurp(s);
//		File f = new File(s);
//		findWin(new FileInputStream(f));
//		new File(s).delete();
//	}


	
	private SystemDnsServers()
	{
	}
}
