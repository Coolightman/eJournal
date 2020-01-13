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

/**
 * The type Generic service.
 *
 * @param <T> the type parameter
 */
@Service
@Transactional
public abstract class GenericServiceImpl<T extends BaseClass> implements GenericService<T> {

    /**
     * The Admin repository.
     */
    final AdminRepository adminRepository;
    /**
     * The A class repository.
     */
    final AClassRepository AClassRepository;
    /**
     * The Discipline repository.
     */
    final DisciplineRepository disciplineRepository;
    /**
     * The Grade repository.
     */
    final GradeRepository gradeRepository;
    /**
     * The Parent repository.
     */
    final ParentRepository parentRepository;
    /**
     * The Pupil repository.
     */
    final PupilRepository pupilRepository;
    /**
     * The Role repository.
     */
    final RoleRepository roleRepository;
    /**
     * The Teacher repository.
     */
    final TeacherRepository teacherRepository;
    /**
     * The Localized message source.
     */
    final LocalizedMessageSource localizedMessageSource;

    /**
     * Instantiates a new Generic service.
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
     */
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
        findRepository(type).deleteById(id);
    }

    /**
     * Validate.
     *
     * @param expression   the expression
     * @param errorMessage the error message
     */
    public void validate(boolean expression, String errorMessage) {
        if (expression) {
            throw getRuntimeException(errorMessage);
        }
    }

    /**
     * Gets runtime exception.
     *
     * @param s the s
     * @return the runtime exception
     */
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

