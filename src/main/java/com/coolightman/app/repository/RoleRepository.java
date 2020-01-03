package com.coolightman.app.repository;

import com.coolightman.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameIgnoreCase(final String name);

    boolean existsByNameIgnoreCase(final String name);

    List<Role> findAllByIdIsNotNullOrderByName();
}
