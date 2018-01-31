package space.dcce.commons.persistence;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

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
