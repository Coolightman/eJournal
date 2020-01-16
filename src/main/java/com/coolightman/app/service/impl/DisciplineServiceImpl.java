package com.coolightman.app.service.impl;

import com.coolightman.app.component.LocalizedMessageSource;
import com.coolightman.app.model.Discipline;
import com.coolightman.app.model.Teacher;
import com.coolightman.app.repository.*;
import com.coolightman.app.service.DisciplineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * The type Discipline service.
 */
@Service
@Transactional
public class DisciplineServiceImpl extends GenericServiceImpl<Discipline> implements DisciplineService {

    private Class type = Discipline.class;

    /**
     * Instantiates a new Discipline service.
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
        String currentDiscName = findByID(discipline.getId()).getName();
        if (discipline.getName().equals(currentDiscName)) {
            return super.update(discipline, type);
        } else {
            validate(existByName(discipline.getName()), "error.discipline.name.notUnique");
            return super.update(discipline, type);
        }
    }

    @Override
    public List<Discipline> findAll() {
        return disciplineRepository.findAllByOrderByName();
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
        Discipline discipline = disciplineRepository.findById(id).get();
        gradeRepository.findByDisciplineOrderByPupil(discipline).forEach(gradeRepository::delete);
        teacherRepository.findAll()
                .stream()
                .map(Teacher::getDisciplines)
                .forEach(disciplines -> {

                    Iterator i = disciplines.iterator();
                    Discipline disc;

                    while (i.hasNext()) {
                        disc = (Discipline) i.next();
                        if (disc.equals(discipline)) {
                            i.remove();
                            break;
                        }
                    }
                });

        super.deleteByID(id, type);
    }

}
