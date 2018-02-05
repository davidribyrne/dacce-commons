package space.dcce.commons.data_model.encoding;

import java.io.IOException;
import java.util.Stack;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;

import space.dcce.commons.data_model.Node;


public class JSONUtils
{

	private JSONUtils()
	{
	}
	
	public static boolean isValidJSON(byte[] data)
	{
		JsonFactory factory = new JsonFactory();
		try
		{
			JsonParser parser = factory.createParser(data);
			while (!parser.isClosed())
			{
				JsonToken token = parser.nextToken();
			}
		}
		catch(IOException e)
		{
			return false;
		}
		return true;
	}

	public static byte[] encode(JSONEncoding data) throws IOException
	{
		ByteArrayBuilder output = new ByteArrayBuilder();
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createGenerator(output);
		_encode(data.getChildNode(), generator);
		generator.close();
		return output.toByteArray();
	}
	
	private static void _encode(Node node, JsonGenerator generator) throws IOException
	{
		switch(node.getNodeType())
		{
			case LIST:
				generator.writeStartArray();
				ListNode list = (ListNode) node;
				for(Node value: list.getValues())
				{
					_encode(value, generator);
				}
				generator.writeEndArray();
				return;

			case OBJECT:
				generator.writeStartObject();
				ObjectNode object = (ObjectNode) node;
				for(MessageNameValuePair property: object.getProperties())
				{
					_encode(property, generator);
				}
				generator.writeEndObject();
				return;
				
			case PAIR:
				MessageNameValuePair pair = (MessageNameValuePair) node;

				generator.writeFieldName(pair.getName());
				_encode(pair.getValue(), generator);
				return;
			
			case STRING:
				StringPrimitive string = (StringPrimitive) node;
				generator.writeString(string.getString());
				return;
				
			case ENCODING:
				EncodingNode en = (EncodingNode) node;
				generator.writeString(new String(en.getPackagedBytes()));
				return;
			
			case BOOLEAN:
				BooleanPrimitive bp = (BooleanPrimitive) node;
				generator.writeBoolean(bp.getValue());
				return;
			
			case FLOAT:
				FloatPrimitive fp = (FloatPrimitive) node;
				generator.writeNumber(fp.getValue());
				return;
				
			case INTEGER:
				IntegerPrimitive ip = (IntegerPrimitive) node;
				generator.writeNumber(ip.getValue());
				return;
				
			case NULL:
				generator.writeNull();
				return;
				
			default:
				System.err.println("oops");
				
		}
	}
	
	public static JSONEncoding decode(byte[] data) throws JsonParseException, IOException
	{
		JSONEncoding parentNode = null;
		JsonFactory factory = new JsonFactory();
		JsonParser parser = factory.createParser(data);
		Stack<ContainerNode> stack = new Stack<ContainerNode>();
		while (!parser.isClosed())
		{
			JsonToken token = parser.nextToken();
			if (token == null)
			{
				break;
			}
			boolean addedData = false;
			ContainerNode lastContainer = null;
			if (!stack.isEmpty())
			{
				lastContainer = stack.peek();
			}
			switch (token)
			{
				case NOT_AVAILABLE:
					System.err.println("NOT_AVAILABLE");
					break;
					
				case FIELD_NAME:
				{
					MessageNameValuePair pair = new MessageNameValuePair();
					pair.setName(parser.getCurrentName());
					ObjectNode o = (ObjectNode) stack.lastElement();
					o.addProperty(pair);
					stack.push(pair);
					addedData = true;
				}
					break;
					
				case END_ARRAY:
				{
					stack.pop();
				}
					break;

				case END_OBJECT:
				{
					stack.pop();
				}
					break;

				case START_ARRAY:
				{
					ListNode list = new ListNode();

					ContainerNode container = (ContainerNode) stack.lastElement();
					container.addValue(list);
					stack.push(list);
					addedData = true;
				}
					break;

				case START_OBJECT:
				{
					ObjectNode object = new ObjectNode();
					if(stack.isEmpty())
					{
						if (parentNode == null)
						{
							parentNode = new JSONEncoding(object);
						}
						else
						{
							System.err.println("Problem with root object");
						}
					}
					else
					{
						ContainerNode container = (ContainerNode) stack.lastElement();
						container.addValue(object);
						addedData = true;
					}
					stack.push(object);
				}
					break;

				case VALUE_EMBEDDED_OBJECT:
					System.err.println("VALUE_EMBEDDED_OBJECT");
					addedData = true;
					break;

				case VALUE_FALSE:
				{
					BooleanPrimitive bp = new BooleanPrimitive(false);
					ContainerNode container = (ContainerNode) stack.lastElement();
					container.addValue(bp);
					addedData = true;
				}
					break;

				case VALUE_NULL:
				{
					NullPrimitive np = new NullPrimitive();
					ContainerNode container = (ContainerNode) stack.lastElement();
					container.addValue(np);
					addedData = true;
				}
					break;

				case VALUE_NUMBER_FLOAT:
				{
					FloatPrimitive fp = new FloatPrimitive(parser.getDoubleValue());
					ContainerNode container = (ContainerNode) stack.lastElement();
					container.addValue(fp);
					addedData = true;
				}
					break;

				case VALUE_NUMBER_INT:
				{
					IntegerPrimitive ip = new IntegerPrimitive(parser.getLongValue());
					ContainerNode container = (ContainerNode) stack.lastElement();
					container.addValue(ip);
					addedData = true;
				}
					break;

				case VALUE_STRING:
				{
					System.err.println("VALUE_STRING");
//					StringValue value = new StringValue(parser.getText());
					String s = parser.getText();
					PrintableNode pn = Decoder.decode(s.getBytes());
					ContainerNode container = (ContainerNode) stack.lastElement();
					container.addValue(pn);
					
					addedData = true;
				}
					break;

				case VALUE_TRUE:
				{
					BooleanPrimitive bp = new BooleanPrimitive(true);
					ContainerNode container = (ContainerNode) stack.lastElement();
					container.addValue(bp);
					addedData = true;
				}
					break;
			}
			
			if (addedData && lastContainer != null)
			{
				if(lastContainer instanceof MessageNameValuePair)
				{
					stack.remove(lastContainer);
				}
			}

		}
		return parentNode;
	}
}
