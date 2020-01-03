package com.coolightman.app.repository;

import com.coolightman.app.model.Teacher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherRepository extends UserRepository<Teacher> {

    List<Teacher> findByFirstNameIgnoreCaseAndSurnameIgnoreCase(final String firstName, final String surname);

    @Query("SELECT teacher FROM Teacher teacher JOIN teacher.disciplines tds WHERE LOWER(tds.name) LIKE LOWER(:name) " +
            "ORDER BY teacher.surname, teacher.firstName")
    List<Teacher> findByDiscipline(@Param("name") final String name);

    List<Teacher> findAllByIdIsNotNullOrderBySurname();
}
