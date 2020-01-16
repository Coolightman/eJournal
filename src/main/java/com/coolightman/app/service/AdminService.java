package com.coolightman.app.service;


import com.coolightman.app.model.Admin;

/**
 * The interface Admin service.
 */
public interface AdminService extends GenericService<Admin> {

    /**
     * Find admin by login admin.
     *
     * @param login the login
     * @return the admin
     */
    Admin findAdminByLogin(final String login);

    /**
     * Exists by login boolean.
     *
     * @param login the login
     * @return the boolean
     */
    boolean existsByLogin(final String login);
}
