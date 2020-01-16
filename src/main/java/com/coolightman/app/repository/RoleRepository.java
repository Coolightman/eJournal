package com.coolightman.app.repository;

import com.coolightman.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Role repository.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Find by name ignore case optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<Role> findByNameIgnoreCase(final String name);

    /**
     * Exists by name ignore case boolean.
     *
     * @param name the name
     * @return the boolean
     */
    boolean existsByNameIgnoreCase(final String name);

    /**
     * Find all order by name list.
     *
     * @return the list
     */
    List<Role> findAllByOrderByName();
}
