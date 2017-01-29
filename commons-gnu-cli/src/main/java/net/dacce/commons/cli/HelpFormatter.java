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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.text.WordUtils;
import net.dacce.commons.general.StringUtils;


public class HelpFormatter
{

	private String newLine = System.getProperty("line.separator");
	private String groupSeperator = "->";


	private String cmdLineSyntax;
	private String header;
	private Options options;
	private String footer;
	private int width = 90;
	private int leftPad = 1;
	private boolean usage;


	public HelpFormatter(int width, String cmdLineSyntax,
			String header, Options options, int leftPad,
			String footer, boolean usage)
	{
		this.cmdLineSyntax = cleanString(cmdLineSyntax);
		this.header = cleanString(header);
		this.options = options;
		this.footer = cleanString(footer);
		this.width = width;
		this.leftPad = leftPad;
		this.usage = usage;
	}


	public static String makeHelp(int width, String cmdLineSyntax,
			String header, Options options, int leftPad,
			String footer)
	{
		HelpFormatter hf = new HelpFormatter(width, cmdLineSyntax, header, options, leftPad, footer, true);
		return hf.printHelp();
	}


	public String printHelp()
	{
		StringBuffer sb = new StringBuffer();

		if (usage)
		{
			sb.append(generateUsage());
		}

		if ((header != null) && (header.trim().length() > 0))
		{
			sb.append(newLine);
			sb.append(newLine);
			sb.append(WordUtils.wrap(header, width));
			sb.append(newLine);
		}

		sb.append(newLine);
		sb.append(printOptions());

		if ((footer != null) && (footer.trim().length() > 0))
		{
			sb.append(newLine);
			sb.append(newLine);
			sb.append(WordUtils.wrap(footer, width));
		}
		return sb.toString();
	}


	private String cleanString(String s)
	{
		if (s == null)
			return "";
		return s.replace('\t', ' ').replaceAll(" {2,}", " ").trim();
	}


	private String generateUsage()
	{
		StringBuffer buff = new StringBuffer("Usage: ").append(cmdLineSyntax).append(" ");

		Collection<ExclusiveOptions> processedGroups = new ArrayList<ExclusiveOptions>();
		boolean first = true;
		for (Option option : options.getAllOptions())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				buff.append(" ");
			}
			ExclusiveOptions group = options.getExclusiveOptionGroup(option);
			if (group != null)
			{
				if (!processedGroups.contains(group))
				{
					processedGroups.add(group);
					appendOptionGroupUsage(buff, group);
				}
			}
			else
			{
				appendOptionUsage(buff, option, option.isRequired());
			}
		}
		return WordUtils.wrap(buff.toString(), width - leftPad, newLine + createPadding(leftPad), true);
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
		return (option.isArgRequired() ? "" : "[") + (longOpt ? "=" : " ") + (StringUtils.isEmptyOrNull(name) ? "arg" : name)
				+ (option.isArgRequired() ? "" : "]");
	}


	private String printOptions()
	{
		StringBuffer sb = new StringBuffer();
		for (OptionContainer child : options.getOptionContainers())
		{
			printOptionContainer(child, "", sb);
		}

		return sb.toString();
	}


	private String printOption(Option option)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(createPadding(leftPad));
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
		sb.append(newLine);
		tab = leftPad + 6;
		sb.append(createPadding(tab));
		String description = option.getDescription();

		if (option.getDefaultValue() != null && !option.getDefaultValue().isEmpty())
		{
			description += " Default is '" + option.getDefaultValue() + "'.";
		}
		sb.append(WordUtils.wrap(description, width - tab, "\n" + createPadding(tab), true));
		return sb.toString();
	}


	private void printOptionContainer(OptionContainer container, String groupChain, StringBuffer sb)
	{
		if (container instanceof OptionGroup)
		{
			OptionGroup group = (OptionGroup) container;
			String newChain = groupChain + (groupChain.isEmpty() ? "" : groupSeperator) + group.getName();
			sb.append(newLine);
			// sb.append(newChain);
			// sb.append(" -- ");
			sb.append(group.getDescription());
			sb.append(":");
			sb.append(newLine);
			for (OptionContainer child : group.getChildren())
			{
				printOptionContainer(child, newChain, sb);
			}
		}

		else // actual option
		{
			sb.append(printOption((Option) container));
			
			// Module sub-options
			if (container instanceof ModuleOptions) 
			{
				ModuleOptions moduleOptions = (ModuleOptions) container;
				Options subOptions = moduleOptions.getSubOptions();
				HelpFormatter moduleFormater = new HelpFormatter(width, "", "", subOptions, leftPad * 3, "", false);
				sb.append(moduleFormater.printHelp());
			}

			sb.append(newLine);
		}
	}


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


}
