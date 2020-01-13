package com.coolightman.app.service;


import com.coolightman.app.model.Pupil;

import java.time.LocalDate;
import java.util.List;

/**
 * The interface Pupil service.
 */
public interface PupilService extends GenericService<Pupil> {
    /**
     * Find by name list.
     *
     * @param firstName the first name
     * @param surname   the surname
     * @return the list
     */
    List<Pupil> findByName(final String firstName,
                           final String surname);

    /**
     * Find by name and dob pupil.
     *
     * @param firstName the first name
     * @param surname   the surname
     * @param date      the date
     * @return the pupil
     */
    Pupil findByNameAndDob(final String firstName,
                           final String surname,
                           final LocalDate date);

    /**
     * Find by class name list.
     *
     * @param name the name
     * @return the list
     */
    List<Pupil> findByClassName(final String name);

    /**
     * Find pupil by login pupil.
     *
     * @param login the login
     * @return the pupil
     */
    Pupil findPupilByLogin(final String login);
}
