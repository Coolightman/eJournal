package com.coolightman.app.service;

import com.coolightman.app.model.User;

/**
 * The interface User service.
 *
 * @param <T> the type parameter
 */
public interface UserService<T extends User> {

    /**
     * Find by login long.
     *
     * @param login the login
     * @return the long
     */
    Long findByLogin(final String login);

    /**
     * Delete by login.
     *
     * @param login the login
     */
    void deleteByLogin(final String login);

    /**
     * Exists by login boolean.
     *
     * @param login the login
     * @return the boolean
     */
    boolean existsByLogin(final String login);
}
