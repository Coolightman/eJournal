package com.coolightman.app.repository;

import com.coolightman.app.model.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Discipline repository.
 */
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    /**
     * Find by name ignore case optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<Discipline> findByNameIgnoreCase(final String name);

    /**
     * Exists by name ignore case boolean.
     *
     * @param name the name
     * @return the boolean
     */
    boolean existsByNameIgnoreCase(final String name);

    /**
     * Find all by id is not null order by name list.
     *
     * @return the list
     */
    List<Discipline> findAllByIdIsNotNullOrderByName();
}
