package com.coolightman.app.repository;

import com.coolightman.app.model.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    Optional<Discipline> findByNameIgnoreCase(final String name);

    boolean existsByNameIgnoreCase(final String name);

    List<Discipline> findAllByIdIsNotNullOrderByName();
}
