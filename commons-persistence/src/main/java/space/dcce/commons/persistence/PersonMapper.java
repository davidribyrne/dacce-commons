package space.dcce.commons.persistence;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

// TODO: Auto-generated Javadoc
/**
 * The Interface PersonMapper.
 */
public interface PersonMapper
{
	
	/**
	 * Gets the person by ID.
	 *
	 * @param id the id
	 * @return the person by ID
	 */
	@Select("SELECT * FROM people where ID = #{ID}")
	Person getPersonByID(int id);
	
	/**
	 * Insert person.
	 *
	 * @param person the person
	 */
	@Insert("INSERT INTO people "
			+ "(id, name, age) "
		+ "VALUES "
			+ "(#{id}, #{name}, #{age})")
	void insertPerson(Person person);
}
