package com.coolightman.app.repository;

import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * The interface Parent repository.
 */
public interface ParentRepository extends UserRepository<Parent> {

    /**
     * Find by pupil optional.
     *
     * @param pupil the pupil
     * @return the optional
     */
    Optional<Parent> findByPupil(final Pupil pupil);

    /**
     * Find by class name list.
     *
     * @param name the name
     * @return the list
     */
    @Query("SELECT parent FROM Parent parent WHERE LOWER(parent.pupil.aClass.name) LIKE LOWER(:name) " +
            "ORDER BY parent.pupil.surname, parent.pupil.firstName")
    List<Parent> findByClassName(@Param("name") final String name);

    /**
     * Exists by pupil boolean.
     *
     * @param pupil the pupil
     * @return the boolean
     */
    boolean existsByPupil(final Pupil pupil);

    /**
     * Delete by pupil.
     *
     * @param pupil the pupil
     */
    void deleteByPupil(final Pupil pupil);
}
