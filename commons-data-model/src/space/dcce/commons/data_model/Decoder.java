package space.dcce.commons.data_model;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParseException;

import space.dcce.commons.data_model.encoding.Base64Encoder;
import space.dcce.commons.data_model.encoding.JSONUtils;
import space.dcce.commons.data_model.primitives.*;


public class Decoder
{
	static public PrintableNode decode(byte[] data) throws JsonParseException, IOException
	{
		if (isJson(data))
		{
			return JSONUtils.decode(data);
		}
		if (isURLQuery(data))
		{
			byte[] decoded = decodeURL(data);
			return new UrlQueryEncoding(decode(decoded));
		}
		if (isURLString(data))
		{
		}
		if (isBase64(data))
		{
			byte[] decoded = decodeBase64(data);
			return new Base64Encoder(decode(decoded));
		}

		return decodePrimitive(data);
	}


	static public PrimitiveNode decodePrimitive(byte[] data)
	{
		String s = new String(data);
		if (s.isEmpty())
		{
			return new NullPrimitive();
		}
		if (s.matches("^\\d+\\.\\d+$"))
		{
			return new FloatPrimitive(Double.valueOf(s));
		}
		if (s.matches("^\\d+$"))
		{
			return new IntegerPrimitive(Long.valueOf(s));
		}
		if (s.matches("^.$"))
		{
			return new BytePrimitive(Byte.valueOf(s));
		}
		if (s.toLowerCase().matches("^true|false|yes|no|on|off|enabled|disabled$"))
		{
			return new BooleanPrimitive(s);
		}
		return new StringPrimitive(s);
	}


	static public byte[] decodeBase64(byte[] data)
	{
		String s = new String(data).replaceAll("\\s+", "");
		return Base64.getDecoder().decode(s);
	}


	static public byte[] decodeURL(byte[] data)
	{
		return URLDecoder.decode(new String(data)).getBytes();
	}


	public static boolean isJson(byte[] data)
	{
		String s = new String(data);
		Pattern p = Pattern.compile("^\\s*(?://)?\\s*[{\\[].*[}\\]]\\s*$", Pattern.DOTALL);
		if (!p.matcher(s).matches())
		{
			return false;
		}

		return JSONUtils.isValidJSON(data);
	}


	public static boolean isBase64(byte[] data)
	{
		String s = new String(data);
		if (!s.matches("^[a-zA-Z0-9+/]+={0,2}$") ||
				!s.matches(".*[a-z].*") ||
				!s.matches(".*[A-Z].*") ||
				!s.matches(".*[0-9].*"))
		{
			return false;
		}

		return true;
	}


	public static boolean isURLString(byte[] data)
	{
		String s = new String(data);
		if (s.matches("^[a-zA-Z0-9%\\.\\-\\*_+]+$") &&
				s.matches("%[a-fA-F0-9]{2}"))
		{
			Pattern p = Pattern.compile("%(..)");
			Matcher m = p.matcher(s);
			while (m.find())
			{
				if (!m.group(1).matches("^[a-fA-F0-9]{2}$"))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}


	public static boolean isURLQuery(byte[] data)
	{
		String s = new String(data);
		if (s.matches("^[a-zA-Z0-9%\\.\\-\\*_+&=]+$") &&
				s.matches("^[a-zA-Z0-9%\\.\\-\\*_+]="))
		{
			for (String parameter : s.split("&"))
			{
				int index = parameter.indexOf("=");
				if (index < 1)
				{
					return false;
				}
				String name = s.substring(0, index - 1);
				String value = s.substring(index + 1);
				if (!isURLString(name.getBytes()) ||
						!isURLString(value.getBytes()))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}


}
