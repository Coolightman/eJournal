package com.coolightman.app.service;

import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;

/**
 * The interface Parent service.
 */
public interface ParentService extends UserService<Parent> {
    /**
     * Find by pupil parent.
     *
     * @param pupil the pupil
     * @return the parent
     */
    Parent findByPupil(final Pupil pupil);

    /**
     * Exist by pupil boolean.
     *
     * @param pupil the pupil
     * @return the boolean
     */
    boolean existByPupil(final Pupil pupil);
}
