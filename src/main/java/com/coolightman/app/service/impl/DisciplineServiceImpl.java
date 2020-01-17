package com.coolightman.app.service.impl;

import com.coolightman.app.model.Discipline;
import com.coolightman.app.repository.DisciplineRepository;
import com.coolightman.app.repository.GradeRepository;
import com.coolightman.app.repository.TeacherRepository;
import com.coolightman.app.service.DisciplineService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Discipline service.
 */
@Service
@Transactional
public class DisciplineServiceImpl extends GenericServiceImpl<Discipline> implements DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final GradeRepository gradeRepository;
    private final TeacherRepository teacherRepository;

    /**
     * Instantiates a new Discipline service.
     *
     * @param repository           the repository
     * @param disciplineRepository the discipline repository
     * @param gradeRepository      the grade repository
     * @param teacherRepository    the teacher repository
     */
    public DisciplineServiceImpl(@Qualifier("disciplineRepository") final JpaRepository<Discipline, Long> repository,
                                 final DisciplineRepository disciplineRepository,
                                 final GradeRepository gradeRepository,
                                 final TeacherRepository teacherRepository) {
        super(repository);
        this.disciplineRepository = disciplineRepository;
        this.gradeRepository = gradeRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Discipline findByName(final String name) {
        return disciplineRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("error.discipline.notExist"));
    }

    @Override
    public boolean existByName(final String name) {
        return disciplineRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public Discipline save(final Discipline discipline) {
        validate(existByName(discipline.getName()), "error.discipline.name.notUnique");
        return super.save(discipline);
    }

    @Override
    public Discipline update(final Discipline discipline) {
        String currentDiscName = findByID(discipline.getId()).getName();

//        do not validate if update with current name
        if (discipline.getName().equals(currentDiscName)) {
            return super.update(discipline);
        } else {
            validate(existByName(discipline.getName()), "error.discipline.name.notUnique");
            return super.update(discipline);
        }
    }

    @Override
    public void deleteByID(final Long id) {
        Discipline discipline = findByID(id);
        gradeRepository.deleteByDiscipline(discipline);
        teacherRepository.findByDiscipline(discipline.getName())
                .forEach(teacher -> teacher.getDisciplines().remove(discipline));
        super.delete(discipline);
    }

    @Override
    public void delete(final Discipline discipline) {
        deleteByID(discipline.getId());
    }
}
