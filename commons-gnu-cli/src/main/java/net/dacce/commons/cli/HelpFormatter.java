/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dacce.commons.cli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.text.WordUtils;


/**
 * A formatter of help messages for command line options.
 *
 * <p>
 * Example:
 * </p>
 *
 * <pre>
 * Options options = new Options();
 * options.addOption(OptionBuilder.withLongOpt(&quot;file&quot;)
 * 		.withDescription(&quot;The file to be processed&quot;)
 * 		.hasArg()
 * 		.withArgName(&quot;FILE&quot;)
 * 		.isRequired()
 * 		.create('f'));
 * options.addOption(OptionBuilder.withLongOpt(&quot;version&quot;)
 * 		.withDescription(&quot;Print the version of the application&quot;)
 * 		.create('v'));
 * options.addOption(OptionBuilder.withLongOpt(&quot;help&quot;).create('h'));
 *
 * String header = &quot;Do something useful with an input file\n\n&quot;;
 * String footer = &quot;\nPlease report issues at http://example.com/issues&quot;;
 *
 * HelpFormatter formatter = new HelpFormatter();
 * formatter.printHelp(&quot;myapp&quot;, header, options, footer, true);
 * </pre>
 *
 * This produces the following output:
 *
 * <pre>
 * usage: myapp -f &lt;FILE&gt; [-h] [-v]
 * Do something useful with an input file
 *
 *  -f,--file &lt;FILE&gt;   The file to be processed
 *  -h,--help
 *  -v,--version       Print the version of the application
 *
 * Please report issues at http://example.com/issues
 * </pre>
 *
 * @version $Id: HelpFormatter.java 1677407 2015-05-03 14:31:12Z britter $
 */
public class HelpFormatter
{

	private String defaultSyntaxPrefix = "usage: ";
	private String defaultNewLine = System.getProperty("line.separator");
	private String defaultArgName = "arg";
	private String groupSeperator = "->";
	/**
	 * Comparator used to sort the options when they output in help text
	 *
	 * Defaults to case-insensitive alphabetical sorting by option key
	 */
	protected Comparator<Option> optionComparator = new OptionComparator();


	/**
	 * Sets the 'syntaxPrefix'.
	 *
	 * @param prefix
	 *            the new value of 'syntaxPrefix'
	 */
	public void setSyntaxPrefix(String prefix)
	{
		defaultSyntaxPrefix = prefix;
	}


	/**
	 * Returns the 'syntaxPrefix'.
	 *
	 * @return the 'syntaxPrefix'
	 */
	public String getSyntaxPrefix()
	{
		return defaultSyntaxPrefix;
	}


	/**
	 * Sets the 'newLine'.
	 *
	 * @param newline
	 *            the new value of 'newLine'
	 */
	public void setNewLine(String newline)
	{
		defaultNewLine = newline;
	}


	/**
	 * Returns the 'newLine'.
	 *
	 * @return the 'newLine'
	 */
	public String getNewLine()
	{
		return defaultNewLine;
	}


	/**
	 * Sets the 'argName'.
	 *
	 * @param name
	 *            the new value of 'argName'
	 */
	public void setArgName(String name)
	{
		defaultArgName = name;
	}


	/**
	 * Returns the 'argName'.
	 *
	 * @return the 'argName'
	 */
	public String getArgName()
	{
		return defaultArgName;
	}


	/**
	 * Comparator used to sort the options when they output in help text.
	 * Defaults to case-insensitive alphabetical sorting by option key.
	 *
	 * @return the {@link Comparator} currently in use to sort the options
	 * @since 1.2
	 */
	public Comparator<Option> getOptionComparator()
	{
		return optionComparator;
	}


	/**
	 * Set the comparator used to sort the options when they output in help text.
	 * Passing in a null comparator will keep the options in the order they were declared.
	 *
	 * @param comparator
	 *            the {@link Comparator} to use for sorting the options
	 * @since 1.2
	 */
	public void setOptionComparator(Comparator<Option> comparator)
	{
		optionComparator = comparator;
	}


	private String cmdLineSyntax;
	private String header;
	private Options options;
	private String footer;
	private int width = 90;
	private int defaultLeftPad = 1;
	private int optionNameWidth = 10;


	public HelpFormatter(int width, String cmdLineSyntax,
			String header, Options options, int leftPad,
			String footer)
	{
		this.cmdLineSyntax = cleanString(cmdLineSyntax);
		this.header = cleanString(header);
		this.options = options;
		this.footer = cleanString(footer);
		this.width = width;
		defaultLeftPad = leftPad;
	}


	public static String makeHelp(int width, String cmdLineSyntax,
			String header, Options options, int leftPad,
			String footer)
	{
		HelpFormatter hf = new HelpFormatter(width, cmdLineSyntax, header, options, leftPad, footer);
		return hf.printHelp();
	}


	private String printHelp()
	{
		StringBuffer sb = new StringBuffer();
		if ((cmdLineSyntax == null) || (cmdLineSyntax.length() == 0))
		{
			throw new IllegalArgumentException("cmdLineSyntax not provided");
		}

		sb.append(printUsage());
		
		if ((header != null) && (header.trim().length() > 0))
		{
			sb.append(getNewLine());
			sb.append(getNewLine());
			sb.append(WordUtils.wrap(header, width));
		}

		sb.append(getNewLine());
		sb.append(getNewLine());
		sb.append(printOptions());

		if ((footer != null) && (footer.trim().length() > 0))
		{
			sb.append(getNewLine());
			sb.append(getNewLine());
			sb.append(WordUtils.wrap(footer, width));
		}
		return sb.toString();
	}

	private String cleanString(String s)
	{
		return s.replace('\t', ' ').replaceAll(" {2,}", " ").trim();
	}

	/**
	 * Prints the usage statement for the specified application.
	 *
	 * @param pw
	 *            The PrintWriter to print the usage statement
	 * @param width
	 *            The number of characters to display per line
	 * @param app
	 *            The application name
	 * @param options
	 *            The command line Options
	 */
	private String printUsage()
	{
		// initialise the string buffer
		StringBuffer buff = new StringBuffer(getSyntaxPrefix()).append(cmdLineSyntax).append(" ");

		// create a list for processed option groups
		Collection<ExclusiveOptions> processedGroups = new ArrayList<ExclusiveOptions>();

		List<Option> optList = new ArrayList<Option>(options.getAllOptions());
		if (getOptionComparator() != null)
		{
			Collections.sort(optList, getOptionComparator());
		}

		boolean first = true;
		for (Option option : optList)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				buff.append(" ");
			}

			// check if the option is part of an OptionGroup
			ExclusiveOptions group = options.getExclusiveOptionGroup(option);

			// if the option is part of a group
			if (group != null)
			{
				// and if the group has not already been processed
				if (!processedGroups.contains(group))
				{
					// add the group to the processed list
					processedGroups.add(group);

					// add the usage clause
					appendOptionGroupUsage(buff, group);
				}

				// otherwise the option was displayed in the group
				// previously so ignore it.
			}

			// if the Option is not part of an OptionGroup
			else
			{
				appendOptionUsage(buff, option, option.isRequired());
			}
		}

		// call printWrapped
//		return printWrapped(defaultWidth, buff.toString().indexOf(' ') + 1, buff.toString());
		return WordUtils.wrap(buff.toString(), width - defaultLeftPad, getNewLine() + createPadding(defaultLeftPad), true);
	}


	/**
	 * Appends the usage clause for an OptionGroup to a StringBuffer.
	 * The clause is wrapped in square brackets if the group is required.
	 * The display of the options is handled by appendOption
	 *
	 * @param buff
	 *            the StringBuffer to append to
	 * @param group
	 *            the group to append
	 * @see #appendOptionUsage(StringBuffer,Option,boolean)
	 */
	private void appendOptionGroupUsage(StringBuffer buff, ExclusiveOptions group)
	{
		if (!group.isRequired())
		{
			buff.append("[");
		}

		List<Option> optList = new ArrayList<Option>(group.getMembers());
		if (getOptionComparator() != null)
		{
			Collections.sort(optList, getOptionComparator());
		}
		// for each option in the OptionGroup
		for (Iterator<Option> it = optList.iterator(); it.hasNext();)
		{
			// whether the option is required or not is handled at group level
			appendOptionUsage(buff, it.next(), true);

			if (it.hasNext())
			{
				buff.append(" | ");
			}
		}

		if (!group.isRequired())
		{
			buff.append("]");
		}
	}


	/**
	 * Appends the usage clause for an Option to a StringBuffer.
	 *
	 * @param buff
	 *            the StringBuffer to append to
	 * @param option
	 *            the Option to append
	 * @param required
	 *            whether the Option is required or not
	 */
	private void appendOptionUsage(StringBuffer buff, Option option, boolean required)
	{
		if (!required)
		{
			buff.append("[");
		}

		if (option.hasLongOpt())
		{
			buff.append("--");
			buff.append(option.getLongOpt());
			buff.append(getArgText(option, true));
			if (option.hasShortOpt())
			{
				buff.append("|");
			}
		}
		if (option.hasShortOpt())
		{
			buff.append("-").append(option.getShortOpt());
			buff.append(getArgText(option, false));
		}

		if (!required)
		{
			buff.append("]");
		}
	}


	private String getArgText(Option option, boolean longOpt)
	{
		if (!option.isArgAccepted())
		{
			return "";
		}
		String name = option.getArgName();
		return (option.isArgRequired() ? "": "[") + (longOpt ? "=" : " ") + ((name == null) || name.isEmpty() ? defaultArgName : name) + (option.isArgRequired() ? "": "]");
	}


	/**
	 * Print the help for the specified Options to the specified writer,
	 * using the specified width, left padding and description padding.
	 *
	 * @param pw
	 *            The printWriter to write the help to
	 * @param width
	 *            The number of characters to display per line
	 * @param options
	 *            The command line Options
	 * @param leftPad
	 *            the number of characters of padding to be prefixed
	 *            to each line
	 * @param descPad
	 *            the number of characters of padding to be prefixed
	 *            to each description line
	 */
	private String printOptions()
	{
		StringBuffer sb = new StringBuffer();
		for (OptionContainer child : options.getOptionContainers())
		{
			printOptionContainer(child, "", sb);
		}

		return sb.toString();
	}


	/**
	 * Print the specified text to the specified PrintWriter.
	 *
	 * @param pw
	 *            The printWriter to write the help to
	 * @param width
	 *            The number of characters to display per line
	 * @param nextLineTabStop
	 *            The position on the next line for the first tab.
	 * @param text
	 *            The text to be written to the PrintWriter
	 */
//	private String printWrapped(int width, int nextLineTabStop, String text)
//	{
//		return renderWrappedTextBlock(width, nextLineTabStop, text);
//	}


	private String printOption(Option option)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(createPadding(defaultLeftPad));
		if (option.hasShortOpt())
		{
			sb.append("-");
			sb.append(option.getShortOpt());
			if (option.hasLongOpt())
			{
				sb.append(", ");
			}
		}
		if (option.hasLongOpt())
		{
			sb.append("--");
			sb.append(option.getLongOpt());
		}

		if (option.isArgAccepted())
		{
			sb.append(getArgText(option, option.hasLongOpt()));
		}
		int tab;
		if (sb.length() > (optionNameWidth - 2))
		{
			sb.append(defaultNewLine);
			tab = optionNameWidth;
		}
		else
		{
			sb.append(" ");
			tab = optionNameWidth - sb.length();
		}
		sb.append(createPadding(tab));
		String description = option.getDescription();

		if (option.getDefaultValue() != null && !option.getDefaultValue().isEmpty())
		{
			description += " Default is '" + option.getDefaultValue() + "'.";
		}
//		sb.append(renderWrappedTextBlock(defaultWidth, optionNameWidth, description));
		sb.append(WordUtils.wrap(description, width - optionNameWidth, "\n" + createPadding(optionNameWidth), true));
		return sb.toString();
	}


	private void printOptionContainer(OptionContainer container, String groupChain, StringBuffer sb)
	{
		if (container instanceof OptionGroup)
		{
			OptionGroup group = (OptionGroup) container;
			String newChain = groupChain + (groupChain.isEmpty() ? "" : groupSeperator) + group.getName();
			sb.append(getNewLine());
//			sb.append(newChain);
//			sb.append(" -- ");
			sb.append(group.getDescription());
			sb.append(":");
			sb.append(getNewLine());
			for (OptionContainer child : group.getChildren())
			{
				printOptionContainer(child, newChain, sb);
			}
		}
		else
			// actual option
		{
			sb.append(printOption((Option) container));
			sb.append(getNewLine());
		}
	}


	/**
	 * Render the specified text and return the rendered Options
	 * in a StringBuffer.
	 *
	 * @param sb
	 *            The StringBuffer to place the rendered text into.
	 * @param width
	 *            The number of characters to display per line
	 * @param nextLineTabStop
	 *            The position on the next line for the first tab.
	 * @param text
	 *            The text to be rendered.
	 *
	 * @return the StringBuffer with the rendered Options contents.
	 */
//	protected String renderWrappedText(int width,
//			int nextLineTabStop, String text)
//	{
//		StringBuffer sb = new StringBuffer();
//		int pos = findWrapPos(text, width, 0);
//
//		if (pos == -1)
//		{
//			sb.append(rtrim(text));
//
//			return sb.toString();
//		}
//		sb.append(rtrim(text.substring(0, pos))).append(getNewLine());
//
//		if (nextLineTabStop >= width)
//		{
//			// stops infinite loop happening
//			nextLineTabStop = 1;
//		}
//
//		// all following lines must be padded with nextLineTabStop space characters
//		final String padding = createPadding(nextLineTabStop);
//
//		while (true)
//		{
//			text = padding + text.substring(pos).trim();
//			pos = findWrapPos(text, width, 0);
//
//			if (pos == -1)
//			{
//				sb.append(text);
//
//				return sb.toString();
//			}
//
//			if ((text.length() > width) && (pos == (nextLineTabStop - 1)))
//			{
//				pos = width;
//			}
//
//			sb.append(rtrim(text.substring(0, pos))).append(getNewLine());
//		}
//	}


	/**
	 * Render the specified text width a maximum width. This method differs
	 * from renderWrappedText by not removing leading spaces after a new line.
	 *
	 * @param sb
	 *            The StringBuffer to place the rendered text into.
	 * @param width
	 *            The number of characters to display per line
	 * @param nextLineTabStop
	 *            The position on the next line for the first tab.
	 * @param text
	 *            The text to be rendered.
	 */
//	private String renderWrappedTextBlock(int width, int nextLineTabStop, String text)
//	{
//		StringBuffer sb = new StringBuffer();
//		try
//		{
//			BufferedReader in = new BufferedReader(new StringReader(text));
//			String line;
//			boolean firstLine = true;
//			while ((line = in.readLine()) != null)
//			{
//				if (!firstLine)
//				{
//					sb.append(getNewLine());
//				}
//				else
//				{
//					firstLine = false;
//				}
//				sb.append(renderWrappedText(width, nextLineTabStop, line));
//			}
//		}
//		catch (IOException e) // NOPMD
//		{
//			// cannot happen
//		}
//		return sb.toString();
//	}


	/**
	 * Finds the next text wrap position after <code>startPos</code> for the
	 * text in <code>text</code> with the column width <code>width</code>.
	 * The wrap point is the last position before startPos+width having a
	 * whitespace character (space, \n, \r). If there is no whitespace character
	 * before startPos+width, it will return startPos+width.
	 *
	 * @param text
	 *            The text being searched for the wrap position
	 * @param width
	 *            width of the wrapped text
	 * @param startPos
	 *            position from which to start the lookup whitespace
	 *            character
	 * @return position on which the text must be wrapped or -1 if the wrap
	 *         position is at the end of the text
	 */
//	protected int findWrapPos(String text, int width, int startPos)
//	{
//		// the line ends before the max wrap pos or a new line char found
//		int pos = text.indexOf('\n', startPos);
//		if ((pos != -1) && (pos <= width))
//		{
//			return pos + 1;
//		}
//
//		pos = text.indexOf('\t', startPos);
//		if ((pos != -1) && (pos <= width))
//		{
//			return pos + 1;
//		}
//
//		if ((startPos + width) >= text.length())
//		{
//			return -1;
//		}
//
//		// look for the last whitespace character before startPos+width
//		for (pos = startPos + width; pos >= startPos; --pos)
//		{
//			final char c = text.charAt(pos);
//			if ((c == ' ') || (c == '\n') || (c == '\r'))
//			{
//				break;
//			}
//		}
//
//		// if we found it - just return
//		if (pos > startPos)
//		{
//			return pos;
//		}
//
//		// if we didn't find one, simply chop at startPos+width
//		pos = startPos + width;
//
//		return pos == text.length() ? -1 : pos;
//	}


	/**
	 * Return a String of padding of length <code>len</code>.
	 *
	 * @param len
	 *            The length of the String of padding to create.
	 *
	 * @return The String of padding
	 */
	protected String createPadding(int len)
	{
		char[] padding = new char[len];
		Arrays.fill(padding, ' ');

		return new String(padding);
	}


	/**
	 * Remove the trailing whitespace from the specified String.
	 *
	 * @param s
	 *            The String to remove the trailing padding from.
	 *
	 * @return The String of without the trailing padding
	 */
//	protected String rtrim(String s)
//	{
//		if ((s == null) || (s.length() == 0))
//		{
//			return s;
//		}
//
//		int pos = s.length();
//
//		while ((pos > 0) && Character.isWhitespace(s.charAt(pos - 1)))
//		{
//			--pos;
//		}
//
//		return s.substring(0, pos);
//	}

	// ------------------------------------------------------ Package protected
	// ---------------------------------------------------------------- Private
	// ---------------------------------------------------------- Inner classes
	/**
	 * This class implements the <code>Comparator</code> interface
	 * for comparing Options.
	 */
	private static class OptionComparator implements Comparator<Option>, Serializable
	{
		/** The serial version UID. */
		private static final long serialVersionUID = 5305467873966684014L;


		/**
		 * Compares its two arguments for order. Returns a negative
		 * integer, zero, or a positive integer as the first argument
		 * is less than, equal to, or greater than the second.
		 *
		 * @param opt1
		 *            The first Option to be compared.
		 * @param opt2
		 *            The second Option to be compared.
		 * @return a negative integer, zero, or a positive integer as
		 *         the first argument is less than, equal to, or greater than the
		 *         second.
		 */
		public int compare(Option opt1, Option opt2)
		{
			return opt1.getName().compareToIgnoreCase(opt2.getName());
		}
	}

}
