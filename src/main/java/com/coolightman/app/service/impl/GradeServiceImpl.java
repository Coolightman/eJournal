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

@Service
@Transactional
public class GradeServiceImpl extends GenericServiceImpl<Grade> implements GradeService {
    private Class type = Grade.class;

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
        final List<Grade> grades = gradeRepository.findByDisciplineOrderByPupil(discipline);
        validateForEmptyList(grades);
        return grades;
    }

    @Override
    public List<Grade> findByDisciplineAndDate(final Discipline discipline,
                                               final LocalDate date) {
        final List<Grade> grades = gradeRepository.findByDisciplineAndDateOrderByPupil(discipline, date);
        validateForEmptyList(grades);
        return grades;
    }

    @Override
    public List<Grade> findByDisciplineAndDatePeriod(final Discipline discipline,
                                                     final LocalDate date,
                                                     final LocalDate date2) {
        final List<Grade> grades = gradeRepository
                .findByDisciplineAndDateAfterAndDateBeforeOrderByPupil(discipline, date, date2);
        validateForEmptyList(grades);
        return grades;
    }

    @Override
    public List<Grade> findByPupil(final Pupil pupil) {
        final List<Grade> grades = gradeRepository.findByPupilOrderByDate(pupil);
        validateForEmptyList(grades);
        return grades;
    }

    @Override
    public List<Grade> findByPupilAndDate(final Pupil pupil, final LocalDate date) {
        final List<Grade> grades = gradeRepository.findByPupilAndDateOrderByDiscipline(pupil, date);
        validateForEmptyList(grades);
        return grades;
    }

    @Override
    public List<Grade> findByPupilAndDatePeriod(final Pupil pupil,
                                                final LocalDate date,
                                                final LocalDate date2) {
        final List<Grade> grades = gradeRepository
                .findByPupilAndDateAfterAndDateBeforeOrderByDate(pupil, date, date2);
        validateForEmptyList(grades);
        return grades;
    }

    @Override
    public List<Grade> findByPupilAndDiscipline(final Pupil pupil,
                                                final Discipline discipline) {
        final List<Grade> grades = gradeRepository
                .findByPupilAndDisciplineOrderByDate(pupil, discipline);
        validateForEmptyList(grades);
        return grades;
    }

    @Override
    public List<Grade> findByPupilDisciplineAndDate(final Pupil pupil,
                                                    final Discipline discipline,
                                                    final LocalDate date) {
        final List<Grade> grades = gradeRepository
                .findByPupilAndDisciplineAndDate(pupil, discipline, date);
        validateForEmptyList(grades);
        return grades;
    }

    @Override
    public List<Grade> findByPupilDisciplineAndDatePeriod(final Pupil pupil,
                                                          final Discipline discipline,
                                                          final LocalDate date,
                                                          final LocalDate date2) {
        final List<Grade> grades = gradeRepository
                .findByPupilAndDisciplineAndDateAfterAndDateBeforeOrderByDate(pupil, discipline, date, date2);
        validateForEmptyList(grades);
        return grades;
    }

    @Override
    public List<Grade> findByClassAndDiscipline(final AClass aClass,
                                                final Discipline discipline) {
        final List<Grade> grades = gradeRepository.findByDisciplineAndClass(discipline, aClass);
        validateForEmptyList(grades);
        return grades;
    }

    @Override
    public List<Grade> findByClassDisciplineAndDate(final AClass aClass,
                                                    final Discipline discipline,
                                                    final LocalDate date) {
        final List<Grade> grades = gradeRepository.findByDisciplineAndClass(discipline, aClass, date);
        validateForEmptyList(grades);
        return grades;
    }

    @Override
    public List<Grade> findByClassDisciplineAndDatePeriod(final AClass aClass,
                                                          final Discipline discipline,
                                                          final LocalDate date,
                                                          final LocalDate date2) {
        final List<Grade> grades = gradeRepository.findByDisciplineAndClass(discipline, aClass, date, date2);
        validateForEmptyList(grades);
        return grades;
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
        existGrade(grade);
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

    private void validateForEmptyList(final List<Grade> grades) {
        validate(grades.size() == 0, "error.grade.notExist");
    }

}
