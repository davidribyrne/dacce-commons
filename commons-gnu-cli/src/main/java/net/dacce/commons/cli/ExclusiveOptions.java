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
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import net.dacce.commons.cli.exceptions.AlreadySelectedException;


/**
 * A group of mutually exclusive options.
 *
 * @version $Id: OptionGroup.java 1669814 2015-03-28 18:09:26Z britter $
 */
public class ExclusiveOptions extends AbstractGroup implements Serializable
{

	private final List<Option> members;


	public ExclusiveOptions(String name, String description)
	{
		super(name, description);
		members = new ArrayList<Option>();
	}

	/** The serial version UID. */
	private static final long serialVersionUID = 1L;


	/** the name of the selected option */
	private Option selected;

	/** specified whether this group is required */
	private boolean required;


	/**
	 * Set the selected option of this group to <code>name</code>.
	 *
	 * @param option
	 *            the option that is selected
	 * @throws AlreadySelectedException
	 *             if an option from this group has
	 *             already been selected.
	 */
	public void setSelected(Option option) throws AlreadySelectedException
	{
		if (option == null)
		{
			// reset the option previously selected
			selected = null;
			return;
		}

		// if no option has already been selected or the
		// same option is being reselected then set the
		// selected member variable
		if ((selected == null) || selected.equals(option.getName()))
		{
			selected = option;
		}
		else
		{
			throw new AlreadySelectedException(this, option);
		}
	}


	/**
	 * @return the selected option name
	 */
	public Option getSelected()
	{
		return selected;
	}


	/**
	 * @param required
	 *            specifies if this group is required
	 */
	public void setRequired(boolean required)
	{
		this.required = required;
	}


	/**
	 * Returns whether this option group is required.
	 *
	 * @return whether this option group is required
	 */
	public boolean isRequired()
	{
		return required;
	}


	public List<Option> getMembers()
	{
		return members;
	}


	public void addMember(Option option)
	{
		if (option != null)
		{
			members.add(option);
		}
	}
	
	@Override
	public String toString()
	{
		ToStringBuilder tsb = new ToStringBuilder(this);
		for(Option option: members)
		{
			tsb.append("member", option.getName());
		}
		tsb.appendSuper(super.toString());
		return tsb.build();
	}
	
}
