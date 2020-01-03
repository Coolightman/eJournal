package com.coolightman.app.repository;

import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParentRepository extends UserRepository<Parent> {

    Optional<Parent> findByPupil(final Pupil pupil);

    @Query("SELECT parent FROM Parent parent WHERE LOWER(parent.pupil.aClass.name) LIKE LOWER(:name) " +
            "ORDER BY parent.pupil.surname, parent.pupil.firstName")
    List<Parent> findByClassName(@Param("name") final String name);

    boolean existsByPupil(final Pupil pupil);
}
