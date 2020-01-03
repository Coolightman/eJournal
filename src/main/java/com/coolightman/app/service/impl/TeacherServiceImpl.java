package com.coolightman.app.service.impl;

import com.coolightman.app.component.LocalizedMessageSource;
import com.coolightman.app.model.Role;
import com.coolightman.app.model.Teacher;
import com.coolightman.app.repository.*;
import com.coolightman.app.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl extends UserServiceImpl<Teacher> implements TeacherService {

    private Class<Teacher> type = Teacher.class;

    public TeacherServiceImpl(final LocalizedMessageSource localizedMessageSource,
                              final AdminRepository adminRepository,
                              final AClassRepository AClassRepository,
                              final DisciplineRepository disciplineRepository,
                              final GradeRepository gradeRepository,
                              final ParentRepository parentRepository,
                              final PupilRepository pupilRepository,
                              final RoleRepository roleRepository,
                              final TeacherRepository teacherRepository,
                              final UserRepository userRepository) {
        super(localizedMessageSource, adminRepository,
                AClassRepository, disciplineRepository,
                gradeRepository, parentRepository,
                pupilRepository, roleRepository,
                teacherRepository, userRepository);
    }

    @Override
    public List<Teacher> findByName(final String firstName, final String surname) {
        final List<Teacher> teachers = teacherRepository
                .findByFirstNameIgnoreCaseAndSurnameIgnoreCase(firstName, surname);
        validate(teachers.size() == 0, "error.teacher.notExist");
        return teachers;
    }

    @Override
    public List<Teacher> findByDisciplineName(final String name) {
        disciplineRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> getRuntimeException("error.discipline.notExist"));
        final List<Teacher> teachers = teacherRepository.findByDiscipline(name);
        validate(teachers.size() == 0, "error.teacher.notExist");
        return teachers;
    }

    @Override
    public Teacher findTeacherByLogin(final String login) {
        Long ID = super.findByLogin(login);
        return findByID(ID);
    }

    @Override
    public Teacher save(final Teacher teacher) {
        return super.save(teacher, type);
    }

    @Override
    public Teacher update(final Teacher teacher) {
        Role role = roleRepository.findByNameIgnoreCase("ROLE_TEACHER").get();
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        teacher.setRoles(roles);
        return super.update(teacher, type);
    }

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAllByIdIsNotNullOrderBySurname();
    }

    @Override
    public Teacher findByID(final Long id) {
        return super.findByID(id, type);
    }

    @Override
    public void delete(final Teacher teacher) {
        super.delete(teacher, type);
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
