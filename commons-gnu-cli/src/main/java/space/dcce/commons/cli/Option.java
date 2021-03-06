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
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import space.dcce.commons.cli.exceptions.ParseException;
import space.dcce.commons.general.CollectionUtils;
import space.dcce.commons.general.StringUtils;
import space.dcce.commons.validators.ValidationException;
import space.dcce.commons.validators.Validator;


// TODO: Auto-generated Javadoc
/**
 * Describes a single command-line option. It maintains
 * information regarding the short-name of the option, the long-name,
 * if any exists, a flag indicating if an argument is required for
 * this option, and a self-documenting description of the option.
 * <p>
 * An Option is not created independently, but is created through an instance of {@link RootOptions}. An Option is required
 * to have at least a short or a long-name.
 * <p>
 * <b>Note:</b> once an {@link Option} has been added to an instance of {@link RootOptions}, it's required flag may not be
 * changed anymore.
 *
 * @version $Id: Option.java 1677406 2015-05-03 14:27:31Z britter $
 * @see space.dcce.commons.cli.RootOptions
 * see org.apache.commons.CommandLine.CommandLine
 */
public class Option extends OptionContainer implements Cloneable, Serializable 
{

	/** The serial version UID. */
	private static final long serialVersionUID = 1L;

	/**  the long representation of the option. */
	private final String longOpt;

	/** The short opt. */
	private final String shortOpt;

	/**  the name of the argument for this option. */
	private String argTypeDescriptor;

	/**  description of the option. */
	private String description;

	/**  specifies whether this option is required to be present. */
	private boolean required;

	/**  specifies whether the argument value of this Option is optional. */
	private boolean argRequired;
	
	/** The multiple calls. */
	private boolean multipleCalls;
	
	/** The arg accepted. */
	private boolean argAccepted;
	
	/** The force enabled. */
	private boolean forceEnabled;

	/**  the list of argument values *. */
	private final List<String> values = new ArrayList<String>();
	
	/** The default value. */
	private String defaultValue;

	/** The call count. */
	private int callCount = 0;

	/** The validators. */
	private final List<Validator> validators = new ArrayList<Validator>(1);

	/**
	 * Instantiates a new option.
	 *
	 * @param root the root
	 * @param shortOption the short option
	 * @param longOption the long option
	 * @param description the description
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	protected Option(RootOptions root, String shortOption, String longOption, String description)
			throws IllegalArgumentException
	{
		this(root, shortOption, longOption, description, false, false, "", "");
	}


	/**
	 * Instantiates a new option.
	 *
	 * @param root the root
	 * @param shortOption the short option
	 * @param longOption the long option
	 * @param description the description
	 * @param argAccepted the arg accepted
	 * @param argRequired the arg required
	 * @param defaultValue the default value
	 * @param argDescription the arg description
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	protected Option(RootOptions root, String shortOption, String longOption, String description, boolean argAccepted, boolean argRequired, String defaultValue, String argDescription)
			throws IllegalArgumentException
	{
		super(longOption, root);
		if (longOption == null || longOption.isEmpty())
		{
			throw new IllegalArgumentException("Long option must be defined");
		}
		
		if (shortOption == null)
			shortOption = "";

//		if (longOption.isEmpty() && shortOption.isEmpty())
//			throw new IllegalArgumentException("The long or short option must be set");

		if (shortOption.length() > 1)
			throw new IllegalArgumentException("The short option can only be one character in length.");


		validateOption(longOption);
		validateOption(shortOption);

		this.defaultValue = defaultValue;
		shortOpt = shortOption;
		longOpt = longOption;
		this.argAccepted = argAccepted;
		this.argRequired = argRequired;
		this.description = description;
		argTypeDescriptor = argDescription;
	}

	/**
	 * Adds the validator.
	 *
	 * @param validator the validator
	 */
	public void addValidator(Validator validator)
	{
		validators.add(validator);
	}
	
	/**
	 * Validate option.
	 *
	 * @param s the s
	 */
	private void validateOption(String s)
	{
		if (!s.isEmpty() && !s.matches("^(?:[?@a-zA-Z0-9]|[\\-_a-zA-Z0-9]+)$"))
		{
			throw new IllegalArgumentException("The '" + s + "' option contains an illegal character.");
		}
	}
	/**
	 * Retrieve the long name of this Option.
	 *
	 * @return Long name of this option, or null, if there is no long name
	 */
	public String getLongOpt()
	{
		return longOpt;
	}


	
	/**
	 * Query to see if this Option has a long name.
	 *
	 * @return boolean flag indicating existence of a long name
	 */
	public boolean hasLongOpt()
	{
		return !StringUtils.isEmptyOrNull(longOpt);
	}


	/**
	 * Notify call.
	 */
	void notifyCall()
	{
		callCount++;
	}


	/**
	 * Checks for short opt.
	 *
	 * @return true, if successful
	 */
	public boolean hasShortOpt()
	{
		return !StringUtils.isEmptyOrNull(shortOpt);
	}


	/**
	 * Retrieve the self-documenting description of this Option.
	 *
	 * @return The string description of this option
	 */
	public String getDescription()
	{
		return description;
	}


	/**
	 * Sets the self-documenting description of this Option.
	 *
	 * @param description            The description of this option
	 * @since 1.1
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}


	/**
	 * Query to see if this Option is mandatory.
	 *
	 * @return boolean flag indicating whether this Option is mandatory
	 */
	public boolean isRequired()
	{
		return required;
	}


	/**
	 * Sets whether this Option is mandatory.
	 *
	 * @param required
	 *            specifies whether this Option is mandatory
	 */
	public void setRequired(boolean required)
	{
		this.required = required;
	}


	/**
	 * Sets the display name for the argument value.
	 *
	 * @param argName
	 *            the display name for the argument value.
	 */
	public void setArgName(String argName)
	{
		argTypeDescriptor = argName;
	}


	/**
	 * Gets the display name for the argument value.
	 *
	 * @return the display name for the argument value.
	 */
	public String getArgName()
	{
		return argTypeDescriptor;
	}


	/**
	 * Returns whether the display name for the argument value has been set.
	 *
	 * @return if the display name for the argument value has been set.
	 */
	public boolean hasArgName()
	{
		return (argTypeDescriptor != null) && (argTypeDescriptor.length() > 0);
	}


	/**
	 * Adds the value.
	 *
	 * @param value the value
	 * @throws ParseException the parse exception
	 */
	public void addValue(String value) throws ParseException
	{
		if (value == null)
			return;
		if (!argAccepted)
		{
			throw new ParseException("The " + getName() + " option does not accept an argument.");
		}
//		if (argRequired && value == null)
//		{
//			throw new ParseException("The " + getName() + " option requires an argument.");
//		}
		if ((values.size() > 0) && !multipleCalls)
		{
			throw new ParseException("The " + getName() + " option may only be called once.");
		}
		for (Validator validator: validators)
		{
			try
			{
				validator.validate(value);
			}
			catch (ValidationException e)
			{
				throw new ParseException(e.getLocalizedMessage(), e);
			}
		}

		values.add(value);
	}

	/**
	 * Is a non-default value set.
	 *
	 * @param includeDefault the include default
	 * @return true, if is value set
	 */
	public boolean isValueSet(boolean includeDefault)
	{
		if (values.isEmpty())
		{
			if (includeDefault)
				return !StringUtils.isEmptyOrNull(defaultValue);
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue()
	{
		if (values.isEmpty())
		{
			return defaultValue;
		}

		return values.get(0);
	}


	/**
	 * Return the values of this Option as a String array
	 * or null if there are no values.
	 *
	 * @return the values of this Option as a String array
	 *         or null if there are no values
	 */
	public List<String> getValues()
	{
		if (values.isEmpty() && defaultValue != null && !defaultValue.isEmpty())
		{
			List<String> l = new ArrayList<String>();
			l.add(defaultValue);
			return l;
		}
		return values;
	}


	/**
	 * Dump state, suitable for debugging.
	 *
	 * @return Stringified form of this object
	 */
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).appendSuper(super.toString()).append("longOpt", longOpt).append("shortOpt", shortOpt)
				.append("argTypeDescriptor", argTypeDescriptor).append("description", description)
				.append("required", required).append("argRequired", argRequired)
				.append("forceEnabled", forceEnabled).append("values", CollectionUtils.joinObjects(",", values))
				.append("defaultValue", defaultValue).append("callCount", callCount).build();
	}


//	@Override
//	public boolean equals(Object o)
//	{
//		if (this == o)
//		{
//			return true;
//		}
//		if ((o == null) || (getClass() != o.getClass()))
//		{
//			return false;
//		}
//
//		Option option = (Option) o;
//
//
//		if (shortOpt != null ? !shortOpt.equals(option.shortOpt) : option.shortOpt != null)
//		{
//			return false;
//		}
//		if (longOpt != null ? !longOpt.equals(option.longOpt) : option.longOpt != null)
//		{
//			return false;
//		}
//
//		return true;
//	}
//
//
//	@Override
//	public int hashCode()
//	{
//		int result;
//		result = shortOpt != null ? shortOpt.hashCode() : 0;
//		result = (31 * result) + (longOpt != null ? longOpt.hashCode() : 0);
//		return result;
//	}


	/**
	 * Clear the Option values. After a parse is complete, these are left with
	 * data in them and they need clearing if another parse is done.
	 *
	 */
	void clearValues()
	{
		callCount = 0;
		values.clear();
	}


	/**
	 * Gets the short opt.
	 *
	 * @return the short opt
	 */
	public String getShortOpt()
	{
		return shortOpt;
	}


	/**
	 * Checks if is arg required.
	 *
	 * @return true, if is arg required
	 */
	public boolean isArgRequired()
	{
		return argRequired;
	}


	/**
	 * Sets the arg required.
	 *
	 * @param argRequired the new arg required
	 */
	public void setArgRequired(boolean argRequired)
	{
		this.argRequired = argRequired;
	}


	/**
	 * Checks if is arg accepted.
	 *
	 * @return true, if is arg accepted
	 */
	public boolean isArgAccepted()
	{
		return argAccepted;
	}


	/**
	 * Sets the arg accepted.
	 *
	 * @param argAccepted the new arg accepted
	 */
	public void setArgAccepted(boolean argAccepted)
	{
		this.argAccepted = argAccepted;
	}


	/**
	 * Checks if is multiple calls.
	 *
	 * @return true, if is multiple calls
	 */
	public boolean isMultipleCalls()
	{
		return multipleCalls;
	}


	/**
	 * Sets the multiple calls.
	 *
	 * @param multipleCalls the new multiple calls
	 */
	public void setMultipleCalls(boolean multipleCalls)
	{
		this.multipleCalls = multipleCalls;
	}


	/**
	 * Gets the default value.
	 *
	 * @return the default value
	 */
	public String getDefaultValue()
	{
		return defaultValue;
	}


	/**
	 * Sets the default value.
	 *
	 * @param defaultValue the new default value
	 */
	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}


	/**
	 * Gets the call count.
	 *
	 * @return the call count
	 */
	public int getCallCount()
	{
		return callCount;
	}


	/**
	 * Force enabled.
	 */
	public void forceEnabled()
	{
		forceEnabled = true;
	}
	
	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled()
	{
		return forceEnabled || (callCount > 0) || (values.size() > 0);
	}


}
