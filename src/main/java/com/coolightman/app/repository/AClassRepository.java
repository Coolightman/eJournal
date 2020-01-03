package com.coolightman.app.repository;

import com.coolightman.app.model.AClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AClassRepository extends JpaRepository<AClass, Long> {
    Optional<AClass> findByNameIgnoreCase(final String name);

    boolean existsByNameIgnoreCase(final String name);

    List<AClass> findAllByIdIsNotNullOrderByName();
}
