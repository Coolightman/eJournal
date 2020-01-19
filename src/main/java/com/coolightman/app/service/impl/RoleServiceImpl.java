package com.coolightman.app.service.impl;

import com.coolightman.app.model.Role;
import com.coolightman.app.repository.RoleRepository;
import com.coolightman.app.service.RoleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Role service.
 */
@Service
@Transactional
public class RoleServiceImpl extends GenericServiceImpl<Role> implements RoleService {

    private final RoleRepository roleRepository;

    /**
     * Instantiates a new Role service.
     *
     * @param repository     the repository
     * @param roleRepository the role repository
     */
    public RoleServiceImpl(@Qualifier("roleRepository") final JpaRepository<Role, Long> repository,
                           final RoleRepository roleRepository) {
        super(repository);
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(final String name) {
        return roleRepository.findByNameIgnoreCase(name).orElseThrow(() -> new RuntimeException("error.role.notExist"));
    }

    @Override
    public boolean existByName(final String name) {
        return roleRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public Role save(final Role role) {
        validate(existByName(role.getName()), "error.role.name.notUnique");
        return super.save(role);
    }

    @Override
    public Role update(final Role role) {
        validate(existByName(role.getName()), "error.role.name.notUnique");
        return super.update(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAllByOrderByName();
    }
}
