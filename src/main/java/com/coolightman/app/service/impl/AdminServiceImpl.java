package com.coolightman.app.service.impl;

import com.coolightman.app.model.Admin;
import com.coolightman.app.model.Role;
import com.coolightman.app.repository.UserRepository;
import com.coolightman.app.service.AdminService;
import com.coolightman.app.service.RoleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Admin service.
 */
@Service
@Transactional
public class AdminServiceImpl extends UserServiceImpl<Admin> implements AdminService {

    private final RoleService roleService;

    /**
     * Instantiates a new Admin service.
     *
     * @param repository     the repository
     * @param userRepository the user repository
     * @param roleService    the role service
     */
    public AdminServiceImpl(@Qualifier("adminRepository") final JpaRepository<Admin, Long> repository,
                            final UserRepository<Admin> userRepository,
                            final RoleService roleService) {
        super(repository, userRepository);
        this.roleService = roleService;
    }

    @Override
    public Admin save(final Admin admin) {
        setRole(admin);
        return super.save(admin);
    }

    @Override
    public Admin update(final Admin admin) {
        setRole(admin);
        return super.update(admin);
    }

    private void setRole(final Admin admin) {
        final List<Role> roles = new ArrayList<>();
        roles.add(roleService.findByName("ROLE_ADMIN"));
        admin.setRoles(roles);
    }
}
