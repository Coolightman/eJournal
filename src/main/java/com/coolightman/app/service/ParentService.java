package com.coolightman.app.service;

import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;

import java.util.List;

/**
 * The interface Parent service.
 */
public interface ParentService extends GenericService<Parent> {
    /**
     * Find by pupil parent.
     *
     * @param pupil the pupil
     * @return the parent
     */
    Parent findByPupil(final Pupil pupil);

    /**
     * Find by pupil class name list.
     *
     * @param name the name
     * @return the list
     */
    List<Parent> findByPupilClassName(final String name);

    /**
     * Exist by pupil boolean.
     *
     * @param pupil the pupil
     * @return the boolean
     */
    boolean existByPupil(final Pupil pupil);

    /**
     * Find parent by login parent.
     *
     * @param login the login
     * @return the parent
     */
    Parent findParentByLogin(final String login);
}
