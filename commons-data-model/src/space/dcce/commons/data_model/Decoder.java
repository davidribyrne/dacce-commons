package space.dcce.commons.data_model;

import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParseException;

import space.dcce.commons.data_model.encoding.Base64Encoder;
import space.dcce.commons.data_model.encoding.JSONUtils;
import space.dcce.commons.data_model.encoding.URLEncodingUtils;
import space.dcce.commons.data_model.encoding.UrlEncoding;
import space.dcce.commons.data_model.primitives.*;


public class Decoder
{
	static public Node decode(byte[] data) throws JsonParseException, IOException
	{
		if (isJson(data))
		{
			return JSONUtils.decode(data);
		}
		if (isURLQuery(data))
		{
			byte[] decoded = URLEncodingUtils.decodeURL(data);
			Node child = decode(decoded);
			if (child instanceof PrintableNode)
			{
				return new UrlEncoding((PrintableNode) child);
			}
			else
			{
				throw new IllegalStateException("Somehow, an unprintable got identified as URL data");
			}
			
		}
		if (isURLString(data))
		{
			
		}
		if (isBase64(data))
		{
			byte[] decoded = decodeBase64(data);
			Node child = decode(decoded);
			if (child instanceof PrintableNode)
			{
				return new Base64Encoder((PrintableNode) child);
			}
			else
			{
				throw new IllegalStateException("Somehow, an unprintable got identified as base64 data");
			}

			
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
