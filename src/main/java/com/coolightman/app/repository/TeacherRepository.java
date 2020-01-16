package com.coolightman.app.repository;

import com.coolightman.app.model.Teacher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The interface Teacher repository.
 */
public interface TeacherRepository extends UserRepository<Teacher> {

    /**
     * Find by first name ignore case and surname ignore case list.
     *
     * @param firstName the first name
     * @param surname   the surname
     * @return the list
     */
    List<Teacher> findByFirstNameIgnoreCaseAndSurnameIgnoreCase(final String firstName, final String surname);

    /**
     * Find by discipline list.
     *
     * @param name the name
     * @return the list
     */
    @Query("SELECT teacher FROM Teacher teacher JOIN teacher.disciplines tds WHERE LOWER(tds.name) LIKE LOWER(:name) " +
            "ORDER BY teacher.surname, teacher.firstName")
    List<Teacher> findByDiscipline(@Param("name") final String name);

    /**
     * Find all order by surname list.
     *
     * @return the list
     */
    List<Teacher> findAllByOrderBySurname();
}
