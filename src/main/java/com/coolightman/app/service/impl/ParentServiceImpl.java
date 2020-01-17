package com.coolightman.app.service.impl;

import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.model.Role;
import com.coolightman.app.repository.ParentRepository;
import com.coolightman.app.repository.UserRepository;
import com.coolightman.app.service.ParentService;
import com.coolightman.app.service.RoleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Parent service.
 */
@Service
@Transactional
public class ParentServiceImpl extends UserServiceImpl<Parent> implements ParentService {

    private final ParentRepository parentRepository;
    private final RoleService roleService;

    /**
     * Instantiates a new Parent service.
     *
     * @param repository       the repository
     * @param userRepository   the user repository
     * @param passwordEncoder  the password encoder
     * @param parentRepository the parent repository
     * @param roleService      the role service
     */
    public ParentServiceImpl(@Qualifier("parentRepository") final JpaRepository<Parent, Long> repository,
                             final UserRepository<Parent> userRepository,
                             final BCryptPasswordEncoder passwordEncoder,
                             final ParentRepository parentRepository,
                             final RoleService roleService) {
        super(repository, userRepository, passwordEncoder);
        this.parentRepository = parentRepository;
        this.roleService = roleService;
    }

    @Override
    public Parent findByPupil(final Pupil pupil) {
        return parentRepository.findByPupil(pupil)
                .orElseThrow(() -> new RuntimeException("error.parent.notExist"));
    }

    @Override
    public boolean existByPupil(final Pupil pupil) {
        return parentRepository.existsByPupil(pupil);
    }

    @Override
    public Parent save(final Parent parent) {
        setRole(parent);
        return super.save(parent);
    }

    @Override
    public Parent update(final Parent parent) {
        setRole(parent);
        return super.update(parent);
    }

    private void setRole(final Parent parent) {
        Role role = roleService.findByName("ROLE_PARENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        parent.setRoles(roles);
    }
}
