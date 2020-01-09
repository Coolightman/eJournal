package com.coolightman.app.service.impl;

import com.coolightman.app.component.LocalizedMessageSource;
import com.coolightman.app.model.*;
import com.coolightman.app.repository.*;
import com.coolightman.app.service.GenericService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public abstract class GenericServiceImpl<T extends BaseClass> implements GenericService<T> {

    final AdminRepository adminRepository;
    final AClassRepository AClassRepository;
    final DisciplineRepository disciplineRepository;
    final GradeRepository gradeRepository;
    final ParentRepository parentRepository;
    final PupilRepository pupilRepository;
    final RoleRepository roleRepository;
    final TeacherRepository teacherRepository;
    final LocalizedMessageSource localizedMessageSource;

    public GenericServiceImpl(final LocalizedMessageSource localizedMessageSource,
                              final AdminRepository adminRepository,
                              final AClassRepository AClassRepository,
                              final DisciplineRepository disciplineRepository,
                              final GradeRepository gradeRepository,
                              final ParentRepository parentRepository,
                              final PupilRepository pupilRepository,
                              final RoleRepository roleRepository,
                              final TeacherRepository teacherRepository) {
        this.localizedMessageSource = localizedMessageSource;
        this.adminRepository = adminRepository;
        this.AClassRepository = AClassRepository;
        this.disciplineRepository = disciplineRepository;
        this.gradeRepository = gradeRepository;
        this.parentRepository = parentRepository;
        this.pupilRepository = pupilRepository;
        this.roleRepository = roleRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public T save(final T t, Class type) {
        String errorMessage = "error." + type.getName().toLowerCase() + ".hasId";
        validate(t.getId() != null, errorMessage);
        return (T) findRepository(type).saveAndFlush(t);
    }

    @Override
    public T update(final T t, Class type) {
        String errorMessage = "error." + type.getName().toLowerCase() + ".notHasId";
        validate(t.getId() == null, errorMessage);
        findByID(t.getId(), type);
        return (T) findRepository(type).saveAndFlush(t);
    }

    @Override
    public List<T> findAll(Class type) {
        return findRepository(type).findAll();
    }

    @Override
    public T findByID(final Long id, Class type) {
        String errorMessage = "error." + type.getName().toLowerCase() + ".notExist";
        Optional<T> t = findRepository(type).findById(id);
        if (t.isPresent()) {
            return t.get();
        } else {
            throw getRuntimeException(errorMessage);
        }
    }

    @Override
    public void delete(final T t, Class type) {
        deleteByID(t.getId(), type);
    }

    @Override
    public void deleteAll(Class type) {
        findRepository(type).deleteAll();
    }

    @Override
    public void deleteByID(final Long id, Class type) {
        findByID(id, type);
        findRepository(type).deleteById(id);
    }

    public void validate(boolean expression, String errorMessage) {
        if (expression) {
            throw getRuntimeException(errorMessage);
        }
    }

    RuntimeException getRuntimeException(final String s) {
        return new RuntimeException(localizedMessageSource.getMessage(s, new Object[]{}));
    }

    private JpaRepository findRepository(final Class type) {
        JpaRepository repository;
        if (type.equals(Admin.class)) {
            repository = adminRepository;
        } else if (type.equals(AClass.class)) {
            repository = AClassRepository;
        } else if (type.equals(Discipline.class)) {
            repository = disciplineRepository;
        } else if (type.equals(Grade.class)) {
            repository = gradeRepository;
        } else if (type.equals(Parent.class)) {
            repository = parentRepository;
        } else if (type.equals(Pupil.class)) {
            repository = pupilRepository;
        } else if (type.equals(Teacher.class)) {
            repository = teacherRepository;
        } else if (type.equals(Role.class)) {
            repository = roleRepository;
        } else {
            throw getRuntimeException("fatality");
        }
        return repository;
    }
}

