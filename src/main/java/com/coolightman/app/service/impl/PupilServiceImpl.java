package com.coolightman.app.service.impl;

import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.model.Role;
import com.coolightman.app.repository.ParentRepository;
import com.coolightman.app.repository.PupilRepository;
import com.coolightman.app.repository.UserRepository;
import com.coolightman.app.service.PupilService;
import com.coolightman.app.service.RoleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Pupil service.
 */
@Service
@Transactional
public class PupilServiceImpl extends UserServiceImpl<Pupil> implements PupilService {

    private final PupilRepository pupilRepository;
    private final ParentRepository parentRepository;
    private final RoleService roleService;

    /**
     * Instantiates a new Pupil service.
     *
     * @param repository       the repository
     * @param userRepository   the user repository
     * @param pupilRepository  the pupil repository
     * @param parentRepository the parent repository
     * @param roleService      the role service
     */
    public PupilServiceImpl(@Qualifier("pupilRepository") final JpaRepository<Pupil, Long> repository,
                            final UserRepository<Pupil> userRepository,
                            final PupilRepository pupilRepository,
                            final ParentRepository parentRepository,
                            final RoleService roleService) {
        super(repository, userRepository);
        this.pupilRepository = pupilRepository;
        this.parentRepository = parentRepository;
        this.roleService = roleService;
    }

    @Override
    public List<Pupil> findByName(final String firstName, final String surname) {
        return pupilRepository
                .findByFirstNameIgnoreCaseAndSurnameIgnoreCaseOrderByDob(firstName, surname);
    }

    @Override
    public Pupil findByNameAndDob(final String firstName, final String surname, final LocalDate date) {
        return pupilRepository.findByFirstNameIgnoreCaseAndSurnameIgnoreCaseAndDob(firstName, surname, date)
                .orElseThrow(() -> new RuntimeException("error.pupil.notExist"));
    }

    @Override
    public List<Pupil> findByClass(final AClass aClass) {
        final String aClassName = aClass.getName();
        return pupilRepository.findByClassName(aClassName);
    }

    @Override
    public Pupil save(final Pupil pupil) {
        setRole(pupil);
        return super.save(pupil);
    }

    @Override
    public Pupil update(final Pupil pupil) {
        setRole(pupil);
        return super.update(pupil);
    }

//    Due to the presence of the pupil -> parent connection, if there is a parent, you must delete it through it

    @Override
    public void delete(final Pupil pupil) {
        deleteByID(pupil.getId());
    }

    @Override
    public void deleteByID(final Long id) {
        final Pupil pupil = findByID(id);

        if (parentRepository.existsByPupil(pupil)) {
            parentRepository.deleteByPupil(pupil);
        } else {
            pupilRepository.delete(pupil);
        }
    }

    private void setRole(final Pupil pupil) {
        final List<Role> roles = new ArrayList<>();
        roles.add(roleService.findByName("ROLE_PUPIL"));
        pupil.setRoles(roles);
    }
}
