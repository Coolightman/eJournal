package com.coolightman.app.service.impl;

import com.coolightman.app.component.LocalizedMessageSource;
import com.coolightman.app.model.Discipline;
import com.coolightman.app.repository.*;
import com.coolightman.app.service.DisciplineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DisciplineServiceImpl extends GenericServiceImpl<Discipline> implements DisciplineService {
    private Class type = Discipline.class;

    public DisciplineServiceImpl(final LocalizedMessageSource localizedMessageSource,
                                 final AdminRepository adminRepository,
                                 final AClassRepository AClassRepository,
                                 final DisciplineRepository disciplineRepository,
                                 final GradeRepository gradeRepository,
                                 final ParentRepository parentRepository,
                                 final PupilRepository pupilRepository,
                                 final RoleRepository roleRepository,
                                 final TeacherRepository teacherRepository) {
        super(localizedMessageSource, adminRepository,
                AClassRepository, disciplineRepository,
                gradeRepository, parentRepository,
                pupilRepository, roleRepository,
                teacherRepository);
    }

    @Override
    public Discipline findByName(final String name) {
        return disciplineRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> getRuntimeException("error.discipline.notExist"));
    }

    @Override
    public boolean existByName(final String name) {
        return disciplineRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public Discipline save(final Discipline discipline) {
        validate(existByName(discipline.getName()), "error.discipline.name.notUnique");
        return super.save(discipline, type);
    }

    @Override
    public Discipline update(final Discipline discipline) {
        validate(existByName(discipline.getName()), "error.discipline.name.notUnique");
        return super.update(discipline, type);
    }

    @Override
    public List<Discipline> findAll() {
        return disciplineRepository.findAllByIdIsNotNullOrderByName();
    }

    @Override
    public Discipline findByID(final Long id) {
        return super.findByID(id, type);
    }

    @Override
    public void delete(final Discipline discipline) {
        super.delete(discipline, type);
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
