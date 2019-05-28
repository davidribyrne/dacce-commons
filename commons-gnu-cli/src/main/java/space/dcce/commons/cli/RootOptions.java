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

package space.dcce.commons.cli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

import space.dcce.commons.cli.exceptions.UnrecognizedOptionException;


/**
 * Main entry-point into the library.
 * <p>
 * Options represents a collection of {@link Option} objects, which describe the possible options for a command-line.
 * <p>
 * It may flexibly parse long and short options, with or without values. Additionally, it may parse only a portion of a
 * commandline, allowing for flexible multi-stage parsing.
 *
 * @see org.apache.commons.CommandLine.CommandLine
 *
 * @version $Id: Options.java 1685376 2015-06-14 09:51:59Z britter $
 */
public class RootOptions extends OptionGroup implements Serializable
{
	public RootOptions()
	{
		super(null, "root", "root options");
		root = this;
	}


	/** The serial version UID. */
	private static final long serialVersionUID = 1L;


	private final Set<OptionContainer> requiredOptions = new HashSet<OptionContainer>();
	private final Map<String, Option> longOpts = new HashMap<String, Option>();
	private final Map<String, Option> shortOpts = new HashMap<String, Option>();
	private final List<Option> allOptions = new ArrayList<Option>();

	
	public void clearValues() throws RuntimeException
	{
		for (Option option : allOptions)
		{
			option.clearValues();
		}
	}

	public boolean hasShortOption(String option)
	{
		return shortOpts.containsKey(option);
	}

	public boolean hasLongOption(String option)
	{
		return shortOpts.keySet().contains(option);
	}

	Option getOption(String s) throws UnrecognizedOptionException
	{
		Option option;
		if (s.length() == 1)
			option = shortOpts.get(s);
		else
			option = longOpts.get(s);
		
		if (option == null)
		{
			throw new UnrecognizedOptionException("The option '" + s + "' is not recognized.");
		}
		return option;
	}


	protected void processOption(Option option)
	{
		if (option.hasLongOpt())
		{
			if (longOpts.containsKey(option.getLongOpt()))
			{
				throw new IllegalArgumentException("The long option " + option.getLongOpt() + " has already been added.");
			}
			longOpts.put(option.getLongOpt(), option);
		}
		if (option.hasShortOpt())
		{
			if (shortOpts.containsKey(option.getShortOpt()))
			{
				throw new IllegalArgumentException("The short option " + option.getShortOpt() + " has already been added.");
			}
			shortOpts.put(option.getShortOpt() + "", option);
		}
		if (option.isRequired())
		{
			requiredOptions.add(option);
		}
		allOptions.add(option);
	}


	protected void processGroup(OptionGroup group)
	{
		
		for (OptionContainer container : group.getChildren())
		{
			if (container instanceof Option)
			{
				processOption((Option) container);
			}
			else
			{
				processGroup((OptionGroup) container);
			}
		}
	}




	/**
	 * Dump state, suitable for debugging.
	 *
	 * @return Stringified form of this object
	 */
	@Override
	public String toString()
	{
		ToStringBuilder tsb = new ToStringBuilder(this);
		for (OptionContainer option: getChildren())
		{
			tsb.append("option", option.getName());
		}
		return tsb.toString();
	}


	public Collection<OptionContainer> getRequiredOptions()
	{
		return requiredOptions;
	}


	public List<Option> getAllOptions()
	{
		return allOptions;
	}



}
