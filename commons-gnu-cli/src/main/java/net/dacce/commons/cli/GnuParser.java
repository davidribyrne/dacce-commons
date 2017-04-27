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
import java.util.Collection;

import net.dacce.commons.cli.exceptions.AlreadySelectedException;
import net.dacce.commons.cli.exceptions.MissingArgumentException;
import net.dacce.commons.cli.exceptions.MissingOptionException;
import net.dacce.commons.cli.exceptions.ParseException;
import net.dacce.commons.cli.exceptions.UnrecognizedOptionException;


/**
 * Default parser.
 *
 * @version $Id: DefaultParser.java 1677454 2015-05-03 17:13:54Z ggregory $
 * @since 1.3
 */
public class GnuParser
{

	private final Collection<String> remainingArguments = new ArrayList<String>();

	/** The current options. */
	protected Options options;

	/**
	 * Flag indicating how unrecognized tokens are handled. <tt>true</tt> to stop
	 * the parsing and add the remaining tokens to the args list. <tt>false</tt> to throw an exception.
	 */
	protected boolean stopAtNonOption;

	/** The token currently processed. */
	protected String currentToken;

	/** The last option parsed. */
	protected Option currentOption;

	/** Flag indicating if tokens should no longer be analyzed and simply added as arguments of the command line. */
	protected boolean skipParsing;

	/** The required options and groups expected to be found when parsing the command line. */
	protected Collection<OptionContainer> expectedOpts;


	private GnuParser(Options options, boolean stopAtNonOption)
	{
		this.options = options;
		this.stopAtNonOption = stopAtNonOption;

		expectedOpts = new ArrayList<OptionContainer>(options.getRequiredOptions());

	}


	private Collection<String> parse(String[] args) throws ParseException
	{
		options.clearValues();

		if (args != null)
		{
			for (String argument : args)
			{
				handleToken(argument);
			}
		}
		handleToken("");
		checkRequiredOptions();

		return remainingArguments;
	}


	public static Collection<String> parse(Options options, String[] args, boolean stopAtNonOption)
			throws ParseException
	{
		GnuParser parser = new GnuParser(options, stopAtNonOption);
		return parser.parse(args);
	}


	/**
	 * Throws a {@link MissingOptionException} if all of the required options
	 * are not present.
	 *
	 * @throws MissingOptionException
	 *             if any of the required Options
	 *             are not present.
	 */
	private void checkRequiredOptions() throws MissingOptionException
	{
		// if there are required options that have not been processed
		if (!expectedOpts.isEmpty())
		{
			throw new MissingOptionException(expectedOpts);
		}
	}


	/**
	 * Throw a {@link MissingArgumentException} if the current option
	 * didn't receive the number of arguments expected.
	 */
	private void checkRequiredArgs() throws ParseException
	{
		if ((currentOption != null) && currentOption.isArgRequired())
		{
			throw new MissingArgumentException(currentOption);
		}
	}


	/**
	 * Signal call to an option with an optional parameter when we detect
	 * that we're moving on to a different option
	 * 
	 * @throws ParseException
	 */
	private void closeoutCurrentOption() throws ParseException
	{
		if (currentOption == null)
			return;
		currentOption.addValue("");
		currentOption = null;
	}


	/**
	 * Handle any command line token.
	 *
	 * @param token
	 *            the command line token to handle
	 * @throws ParseException
	 */
	private void handleToken(String token) throws ParseException
	{
		currentToken = token;

		if (skipParsing)
		{
			remainingArguments.add(token);
		}
		else if ("--".equals(token))
		{
			closeoutCurrentOption();
			skipParsing = true;
		}
		else if ((currentOption != null) && currentOption.isArgAccepted() && isArgument(token))
		{
			currentOption.addValue(stripLeadingAndTrailingQuotes(token));
			currentOption = null;
		}
		else if (token.startsWith("--"))
		{
			closeoutCurrentOption();
			handleLongOption(token.substring(2));
		}
		else if (token.startsWith("-") && !"-".equals(token))
		{
			closeoutCurrentOption();
			handleShortOptionCluster(token.substring(1));
		}
		else
		{
			handleUnknownToken(token);
		}

		if ((currentOption != null) && !currentOption.isArgAccepted())
		{
			currentOption = null;
		}
	}


	/**
	 * Returns true is the token is a valid argument.
	 *
	 * @param token
	 */
	private boolean isArgument(String token)
	{
		return !isOption(token) || isNegativeNumber(token);
	}


	/**
	 * Check if the token is a negative number.
	 *
	 * @param token
	 */
	private boolean isNegativeNumber(String token)
	{
		try
		{
			Double.parseDouble(token);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}


	/**
	 * Tells if the token looks like an option.
	 *
	 * @param token
	 */
	private boolean isOption(String token)
	{
		return isLongOption(token) || isShortOptionCluster(token);
	}


	/**
	 * Tells if the token looks like a short option.
	 *
	 * @param token
	 */
	private boolean isShortOptionCluster(String token)
	{
		// short options (-S, -SV, -S=V, -SV1=V2, -S1S2)
		boolean a = token.startsWith("-");
		boolean b = token.length() >= 2 && options.hasShortOption(token.substring(1, 2));
		return a && b;
	}


	/**
	 * Tells if the token looks like a long option.
	 *
	 * @param token
	 */
	private boolean isLongOption(String token)
	{
		if (!token.startsWith("--") || (token.length() == 2))
		{
			return false;
		}

		int pos = token.indexOf("=");
		String t = pos == -1 ? token : token.substring(0, pos);

		if (options.hasLongOption(t))
		{
			// long or partial long options (--L, -L, --L=V, -L=V, --l, --l=V)
			return true;
		}

		return false;
	}


	/**
	 * Handles an unknown token. If the token starts with a dash an
	 * UnrecognizedOptionException is thrown. Otherwise the token is added
	 * to the arguments of the command line. If the stopAtNonOption flag
	 * is set, this stops the parsing and the remaining tokens are added
	 * as-is in the arguments of the command line.
	 *
	 * @param token
	 *            the command line token to handle
	 */
	private void handleUnknownToken(String token) throws ParseException
	{
		if (token.startsWith("-"))
		{
			throw new UnrecognizedOptionException("This shouldn't happen: Unrecognized option: " + token, token);
		}

		remainingArguments.add(token);
		if (stopAtNonOption)
		{
			skipParsing = true;
		}
	}


	/**
	 * Handles the following tokens:
	 *
	 * --L
	 * --L=V
	 * --L V
	 * --l
	 *
	 * @param token
	 *            the command line token to handle
	 */
	private void handleLongOption(String token) throws ParseException
	{
		if (token.indexOf('=') == -1)
		{
			handleOption(options.getOption(token));
		}
		else
		{
			handleLongOptionWithEqual(token);
		}
	}


	/**
	 * Handles the following tokens:
	 *
	 * --L=V
	 * -L=V
	 * --l=V
	 * -l=V
	 *
	 * @param token
	 *            the command line token to handle
	 */
	private void handleLongOptionWithEqual(String token) throws ParseException
	{
		int pos = token.indexOf('=');
		String value = token.substring(pos + 1);
		String name = token.substring(0, pos);

		Option option = options.getOption(name);

		if (option.isArgAccepted())
		{
			handleOption(option);
			currentOption.addValue(value);
			currentOption = null;
		}
		else
		{
			throw new ParseException("The option " + name + " does not take a value.");
		}
	}


	private void handleShortOptionCluster(String token) throws UnrecognizedOptionException, ParseException
	{
		for (int i = 0; i < token.length(); i++)
		{
			Option shortOption = options.getOption(token.substring(0, 1));
			shortOption.notifyCall();
			if (shortOption.isArgAccepted())
			{
				if (i < token.length())
				{
					shortOption.addValue(token.substring(i + 1));
				}
				else
				{
					currentOption = shortOption;
				}
				break;
			}
		}
	}


	private void handleOption(Option option) throws ParseException
	{
		// check the previous option before handling the next one
		checkRequiredArgs();

		updateRequiredOptions(option);

		if (option.isArgAccepted())
		{
			currentOption = option;
		}
		else
		{
			currentOption = null;
			option.notifyCall();
		}
	}


	/**
	 * Removes the option or its group from the list of expected elements.
	 *
	 * @param option
	 */
	private void updateRequiredOptions(Option option) throws AlreadySelectedException
	{
		if (option.isRequired())
		{
			expectedOpts.remove(option);
		}

		// if the option is in an OptionGroup make that option the selected option of the group
		if (options.getExclusiveOptionGroup(option) != null)
		{
			ExclusiveOptions group = options.getExclusiveOptionGroup(option);

			if (group.isRequired())
			{
				expectedOpts.remove(group);
			}

			group.setSelected(option);
		}
	}


	/**
	 * Remove the leading and trailing quotes from <code>str</code>.
	 * E.g. if str is '"one two"', then 'one two' is returned.
	 *
	 * @param str
	 *            The string from which the leading and trailing quotes
	 *            should be removed.
	 *
	 * @return The string without the leading and trailing quotes.
	 */
	static String stripLeadingAndTrailingQuotes(String str)
	{
		int length = str.length();
		if ((length > 1) && str.startsWith("\"") && str.endsWith("\"") && (str.substring(1, length - 1).indexOf('"') == -1))
		{
			str = str.substring(1, length - 1);
		}

		return str;
	}

}
