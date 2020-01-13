package com.coolightman.app.service.impl;

import com.coolightman.app.component.LocalizedMessageSource;
import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.model.Role;
import com.coolightman.app.repository.*;
import com.coolightman.app.service.ParentService;
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

    private Class<Parent> type = Parent.class;

    /**
     * Instantiates a new Parent service.
     *
     * @param localizedMessageSource the localized message source
     * @param adminRepository        the admin repository
     * @param AClassRepository       the a class repository
     * @param disciplineRepository   the discipline repository
     * @param gradeRepository        the grade repository
     * @param parentRepository       the parent repository
     * @param pupilRepository        the pupil repository
     * @param roleRepository         the role repository
     * @param teacherRepository      the teacher repository
     * @param userRepository         the user repository
     * @param passwordEncoder        the password encoder
     */
    public ParentServiceImpl(final LocalizedMessageSource localizedMessageSource,
                             final AdminRepository adminRepository,
                             final AClassRepository AClassRepository,
                             final DisciplineRepository disciplineRepository,
                             final GradeRepository gradeRepository,
                             final ParentRepository parentRepository,
                             final PupilRepository pupilRepository,
                             final RoleRepository roleRepository,
                             final TeacherRepository teacherRepository,
                             final UserRepository userRepository,
                             final BCryptPasswordEncoder passwordEncoder) {
        super(localizedMessageSource, adminRepository,
                AClassRepository, disciplineRepository,
                gradeRepository, parentRepository,
                pupilRepository, roleRepository,
                teacherRepository, userRepository,
                passwordEncoder);
    }

    @Override
    public Parent findByPupil(final Pupil pupil) {
        return parentRepository.findByPupil(pupil)
                .orElseThrow(() -> getRuntimeException("error.parent.notExist"));
    }

    @Override
    public List<Parent> findByPupilClassName(final String name) {
        AClassRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> getRuntimeException("error.class.notExist"));

        final List<Parent> parents = parentRepository.findByClassName(name);
        validate(parents.size() == 0, "error.parent.notExist");
        return parents;
    }

    @Override
    public boolean existByPupil(final Pupil pupil) {
        return parentRepository.existsByPupil(pupil);
    }

    @Override
    public Parent findParentByLogin(final String login) {
        Long ID = super.findByLogin(login);
        return findByID(ID);
    }

    @Override
    public Parent save(final Parent parent) {
        Role role = roleRepository.findByNameIgnoreCase("ROLE_PARENT").get();
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        parent.setRoles(roles);
        return super.save(parent, type);
    }

    @Override
    public Parent update(final Parent parent) {
        Role role = roleRepository.findByNameIgnoreCase("ROLE_PARENT").get();
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        parent.setRoles(roles);
        return super.update(parent, type);
    }

    @Override
    public List<Parent> findAll() {
        return super.findAll(type);
    }

    @Override
    public Parent findByID(final Long id) {
        return super.findByID(id, type);
    }

    @Override
    public void delete(final Parent parent) {
        super.delete(parent, type);
    }

    @Override
    public void deleteAll() {
        super.deleteAll(type);
    }

    @Override
    public void deleteByID(final Long id) {
        super.deleteByID(id, type);
    }
}
