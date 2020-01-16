package com.coolightman.app.service.impl;

import com.coolightman.app.component.LocalizedMessageSource;
import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Discipline;
import com.coolightman.app.model.Grade;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.repository.*;
import com.coolightman.app.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Grade service.
 */
@Service
@Transactional
public class GradeServiceImpl extends GenericServiceImpl<Grade> implements GradeService {

    private Class type = Grade.class;

    /**
     * Instantiates a new Grade service.
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
    public GradeServiceImpl(final LocalizedMessageSource localizedMessageSource,
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
    public List<Grade> findByDiscipline(final Discipline discipline) {
        return gradeRepository.findByDisciplineOrderByPupil(discipline);
    }

    @Override
    public List<Grade> findByDisciplineAndDate(final Discipline discipline,
                                               final LocalDate date) {
        return gradeRepository.findByDisciplineAndDateOrderByPupil(discipline, date);
    }

    @Override
    public List<Grade> findByDisciplineAndDatePeriod(final Discipline discipline,
                                                     final LocalDate date,
                                                     final LocalDate date2) {
        return gradeRepository
                .findByDisciplineAndDateAfterAndDateBeforeOrderByPupil(discipline, date, date2);
    }

    @Override
    public List<Grade> findByPupil(final Pupil pupil) {
        return gradeRepository.findByPupilOrderByDate(pupil);
    }

    @Override
    public List<Grade> findByPupilAndDate(final Pupil pupil, final LocalDate date) {
        return gradeRepository.findByPupilAndDateOrderByDiscipline(pupil, date);
    }

    @Override
    public List<Grade> findByPupilAndDatePeriod(final Pupil pupil,
                                                final LocalDate date,
                                                final LocalDate date2) {
        return gradeRepository
                .findByPupilAndDateAfterAndDateBeforeOrderByDate(pupil, date, date2);
    }

    @Override
    public List<Grade> findByPupilAndDiscipline(final Pupil pupil,
                                                final Discipline discipline) {
        return gradeRepository
                .findByPupilAndDisciplineOrderByDate(pupil, discipline);
    }

    @Override
    public Grade findByPupilDisciplineAndDate(final Pupil pupil,
                                              final Discipline discipline,
                                              final LocalDate date) {

        return gradeRepository.findByPupilAndDisciplineAndDate(pupil, discipline, date)
                .orElseThrow(() -> getRuntimeException("error.grade.notExist"));
    }

    @Override
    public List<Grade> findByPupilDisciplineAndDatePeriod(final Pupil pupil,
                                                          final Discipline discipline,
                                                          final LocalDate date,
                                                          final LocalDate date2) {
        return gradeRepository
                .findByPupilAndDisciplineAndDateAfterAndDateBeforeOrderByDate(pupil, discipline, date, date2);
    }

    @Override
    public List<Grade> findByClassAndDiscipline(final AClass aClass,
                                                final Discipline discipline) {
        return gradeRepository.findByDisciplineAndClass(discipline, aClass);
    }

    @Override
    public List<Grade> findByClassDisciplineAndDate(final AClass aClass,
                                                    final Discipline discipline,
                                                    final LocalDate date) {
        return gradeRepository.findByDisciplineAndClass(discipline, aClass, date);
    }

    @Override
    public List<Grade> findByClassDisciplineAndDatePeriod(final AClass aClass,
                                                          final Discipline discipline,
                                                          final LocalDate date,
                                                          final LocalDate date2) {
        return gradeRepository.findByDisciplineAndClass(discipline, aClass, date, date2);
    }

    @Override
    public boolean existsByPupilAndDisciplineAndDate(final Pupil pupil,
                                                     final Discipline discipline,
                                                     final LocalDate date) {
        return gradeRepository.existsByPupilAndDisciplineAndDate(pupil, discipline, date);
    }

    @Override
    public Grade save(final Grade grade) {
        existGrade(grade);
        return super.save(grade, type);
    }

    private void existGrade(Grade grade) {
        final boolean exist = gradeRepository.existsByPupilAndDisciplineAndDate(grade.getPupil(),
                grade.getDiscipline(),
                grade.getDate());
        validate(exist, "error.grade.notUnique");
    }

    @Override
    public Grade update(final Grade grade) {
        return super.update(grade, type);
    }

    @Override
    public List<Grade> findAll() {
        return super.findAll(type);
    }

    @Override
    public Grade findByID(final Long id) {
        return super.findByID(id, type);
    }

    @Override
    public void delete(final Grade grade) {
        super.delete(grade, type);
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
