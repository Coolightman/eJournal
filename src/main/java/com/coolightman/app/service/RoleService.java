package com.coolightman.app.service;

import com.coolightman.app.model.Role;

public interface RoleService extends GenericService<Role> {
    Role findByName(final String name);

    boolean existByName(final String name);
}
