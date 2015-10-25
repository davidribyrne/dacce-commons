package net.dacce.commons.validators;



public interface Validator
{
	public void validate(String value) throws ValidationException;
}
