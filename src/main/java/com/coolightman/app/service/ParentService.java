package com.coolightman.app.service;

import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;

import java.util.List;

public interface ParentService extends GenericService<Parent> {
    Parent findByPupil(final Pupil pupil);

    List<Parent> findByPupilClassName(final String name);

    boolean existByPupil(final Pupil pupil);

    Parent findParentByLogin(final String login);
}
