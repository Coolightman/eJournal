package com.coolightman.app.repository;

import com.coolightman.app.model.Pupil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PupilRepository extends UserRepository<Pupil> {

    List<Pupil> findByFirstNameIgnoreCaseAndSurnameIgnoreCaseOrderByDob(final String firstName, final String surname);

    Optional<Pupil> findByFirstNameIgnoreCaseAndSurnameIgnoreCaseAndDob(final String firstName,
                                                                        final String surname,
                                                                        final LocalDate dob);

    @Query("SELECT pupil FROM Pupil pupil WHERE LOWER(pupil.aClass.name) LIKE LOWER(:name) " +
            "ORDER BY pupil.surname, pupil.firstName ")
    List<Pupil> findByClassName(@Param("name") final String name);
}
