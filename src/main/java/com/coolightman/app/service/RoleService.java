package com.coolightman.app.service;

import com.coolightman.app.model.Role;

/**
 * The interface Role service.
 */
public interface RoleService extends GenericService<Role> {
    /**
     * Find by name role.
     *
     * @param name the name
     * @return the role
     */
    Role findByName(final String name);

    /**
     * Exist by name boolean.
     *
     * @param name the name
     * @return the boolean
     */
    boolean existByName(final String name);
}
