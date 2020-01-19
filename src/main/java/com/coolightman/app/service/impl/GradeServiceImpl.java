package com.coolightman.app.service.impl;

import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Discipline;
import com.coolightman.app.model.Grade;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.repository.GradeRepository;
import com.coolightman.app.service.GradeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
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

    private final GradeRepository gradeRepository;

    /**
     * Instantiates a new Grade service.
     *
     * @param repository      the repository
     * @param gradeRepository the grade repository
     */
    public GradeServiceImpl(@Qualifier("gradeRepository") final JpaRepository<Grade, Long> repository,
                            final GradeRepository gradeRepository) {
        super(repository);
        this.gradeRepository = gradeRepository;
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
                .orElseThrow(() -> new RuntimeException("error.grade.notExist"));
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
    public boolean existsByPupilDisciplineAndDate(final Pupil pupil,
                                                  final Discipline discipline,
                                                  final LocalDate date) {
        return gradeRepository.existsByPupilAndDisciplineAndDate(pupil, discipline, date);
    }

    @Override
    public Grade save(final Grade grade) {
        existGrade(grade);
        return super.save(grade);
    }

    private void existGrade(final Grade grade) {
        final boolean exist = gradeRepository.existsByPupilAndDisciplineAndDate(
                grade.getPupil(),
                grade.getDiscipline(),
                grade.getDate());
        validate(exist, "error.grade.notUnique");
    }
}
