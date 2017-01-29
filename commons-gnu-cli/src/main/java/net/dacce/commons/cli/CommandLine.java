
/*
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
 *
 */

package net.dacce.commons.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dacce.commons.cli.exceptions.ParseException;

import java.util.*;
import java.io.File;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.Iterator;


/**
 * Adapted from Ant
 * 
 * @author david
 *
 */
public class CommandLine
{
	/**
	 * Crack a command line.
	 * 
	 * @param toProcess
	 *            the command line to process.
	 * @return the command line broken into strings.
	 *         An empty or null toProcess parameter results in a zero sized array.
	 * @throws ParseException 
	 */
	public static String[] translateCommandline(String toProcess) throws ParseException
	{
		if (toProcess == null || toProcess.length() == 0)
		{
			// no command? no string
			return new String[0];
		}
		// parse with a simple finite state machine

		final int normal = 0;
		final int inQuote = 1;
		final int inDoubleQuote = 2;
		int state = normal;
		final StringTokenizer tok = new StringTokenizer(toProcess, "\"\' ", true);
		final ArrayList<String> result = new ArrayList<String>();
		final StringBuilder current = new StringBuilder();
		boolean lastTokenHasBeenQuoted = false;

		while (tok.hasMoreTokens())
		{
			String nextTok = tok.nextToken();
			switch (state)
			{
				case inQuote:
					if ("\'".equals(nextTok))
					{
						lastTokenHasBeenQuoted = true;
						state = normal;
					}
					else
					{
						current.append(nextTok);
					}
					break;
				case inDoubleQuote:
					if ("\"".equals(nextTok))
					{
						lastTokenHasBeenQuoted = true;
						state = normal;
					}
					else
					{
						current.append(nextTok);
					}
					break;
				default:
					if ("\'".equals(nextTok))
					{
						state = inQuote;
					}
					else if ("\"".equals(nextTok))
					{
						state = inDoubleQuote;
					}
					else if (" ".equals(nextTok))
					{
						if (lastTokenHasBeenQuoted || current.length() != 0)
						{
							result.add(current.toString());
							current.setLength(0);
						}
					}
					else
					{
						current.append(nextTok);
					}
					lastTokenHasBeenQuoted = false;
					break;
			}
		}
		if (lastTokenHasBeenQuoted || current.length() != 0)
		{
			result.add(current.toString());
		}
		if (state == inQuote || state == inDoubleQuote)
		{
			throw new ParseException("unbalanced quotes in " + toProcess);
		}
		return result.toArray(new String[result.size()]);
	}
}
