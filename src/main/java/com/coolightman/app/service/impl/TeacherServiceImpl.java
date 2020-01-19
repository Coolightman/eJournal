package com.coolightman.app.service.impl;

import com.coolightman.app.model.Discipline;
import com.coolightman.app.model.Role;
import com.coolightman.app.model.Teacher;
import com.coolightman.app.repository.DisciplineRepository;
import com.coolightman.app.repository.TeacherRepository;
import com.coolightman.app.repository.UserRepository;
import com.coolightman.app.service.RoleService;
import com.coolightman.app.service.TeacherService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Teacher service.
 */
@Service
@Transactional
public class TeacherServiceImpl extends UserServiceImpl<Teacher> implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final DisciplineRepository disciplineRepository;
    private final RoleService roleService;

    /**
     * Instantiates a new Teacher service.
     *
     * @param repository           the repository
     * @param userRepository       the user repository
     * @param teacherRepository    the teacher repository
     * @param disciplineRepository the discipline repository
     * @param roleService          the role service
     */
    public TeacherServiceImpl(@Qualifier("teacherRepository") final JpaRepository<Teacher, Long> repository,
                              final UserRepository<Teacher> userRepository,
                              final TeacherRepository teacherRepository,
                              final DisciplineRepository disciplineRepository,
                              final RoleService roleService) {
        super(repository, userRepository);
        this.teacherRepository = teacherRepository;
        this.disciplineRepository = disciplineRepository;
        this.roleService = roleService;
    }

    @Override
    public List<Teacher> findByName(final String firstName, final String surname) {
        return teacherRepository
                .findByFirstNameIgnoreCaseAndSurnameIgnoreCase(firstName, surname);
    }

    @Override
    public List<Teacher> findByDiscipline(final Discipline discipline) {
        final String disciplineName = discipline.getName();
        validate(disciplineRepository
                .existsByNameIgnoreCase(disciplineName), "error.discipline.notExist");
        return teacherRepository.findByDiscipline(disciplineName);
    }

    @Override
    public Teacher save(final Teacher teacher) {
        setRole(teacher);
        return super.save(teacher);
    }

    @Override
    public Teacher update(final Teacher teacher) {
        setRole(teacher);
        return super.update(teacher);
    }

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAllByOrderBySurname();
    }

    private void setRole(final Teacher teacher) {
        final List<Role> roles = new ArrayList<>();
        roles.add(roleService.findByName("ROLE_TEACHER"));
        teacher.setRoles(roles);
    }
}
