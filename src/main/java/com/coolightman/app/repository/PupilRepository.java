package com.coolightman.app.repository;

import com.coolightman.app.model.Pupil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The interface Pupil repository.
 */
public interface PupilRepository extends UserRepository<Pupil> {

    /**
     * Find by first name ignore case and surname ignore case order by dob list.
     *
     * @param firstName the first name
     * @param surname   the surname
     * @return the list
     */
    List<Pupil> findByFirstNameIgnoreCaseAndSurnameIgnoreCaseOrderByDob(final String firstName, final String surname);

    /**
     * Find by first name ignore case and surname ignore case and dob optional.
     *
     * @param firstName the first name
     * @param surname   the surname
     * @param dob       the dob
     * @return the optional
     */
    Optional<Pupil> findByFirstNameIgnoreCaseAndSurnameIgnoreCaseAndDob(final String firstName,
                                                                        final String surname,
                                                                        final LocalDate dob);

    /**
     * Find by class name list.
     *
     * @param name the name
     * @return the list
     */
    @Query("SELECT pupil FROM Pupil pupil WHERE LOWER(pupil.aClass.name) LIKE LOWER(:name) " +
            "ORDER BY pupil.surname, pupil.firstName ")
    List<Pupil> findByClassName(@Param("name") final String name);
}
