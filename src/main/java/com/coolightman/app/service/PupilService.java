package com.coolightman.app.service;


import com.coolightman.app.model.Pupil;

import java.time.LocalDate;
import java.util.List;

public interface PupilService extends GenericService<Pupil> {
    List<Pupil> findByName(final String firstName,
                           final String surname);

    Pupil findByNameAndDob(final String firstName,
                           final String surname,
                           final LocalDate date);

    List<Pupil> findByClassName(final String name);

    Pupil findPupilByLogin(final String login);
}
