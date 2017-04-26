package net.dacce.commons.persistence;

import org.apache.ibatis.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public interface PersonMapper
{
	@Select("SELECT * FROM people where ID = #{ID}")
	Person getPersonByID(int id);
	
	@Insert("INSERT INTO people "
			+ "(id, name, age) "
		+ "VALUES "
			+ "(#{id}, #{name}, #{age})")
	void insertPerson(Person person);
}
