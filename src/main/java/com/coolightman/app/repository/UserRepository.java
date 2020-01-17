package com.coolightman.app.repository;

import com.coolightman.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface User repository.
 *
 * @param <T> the type parameter
 */
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    /**
     * Find by login ignore case optional.
     *
     * @param login the login
     * @return the optional
     */
    Optional<T> findByLoginIgnoreCase(final String login);

    /**
     * Exists by login ignore case boolean.
     *
     * @param login the login
     * @return the boolean
     */
    boolean existsByLoginIgnoreCase(final String login);
}
