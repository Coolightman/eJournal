package com.coolightman.app.service;


import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Discipline;
import com.coolightman.app.model.Grade;
import com.coolightman.app.model.Pupil;

import java.time.LocalDate;
import java.util.List;

/**
 * The interface Grade service.
 */
public interface GradeService extends GenericService<Grade> {
    /**
     * Find by discipline list.
     *
     * @param discipline the discipline
     * @return the list
     */
    List<Grade> findByDiscipline(final Discipline discipline);

    /**
     * Find by discipline and date list.
     *
     * @param discipline the discipline
     * @param date       the date
     * @return the list
     */
    List<Grade> findByDisciplineAndDate(final Discipline discipline, final LocalDate date);

    /**
     * Find by discipline and date period list.
     *
     * @param discipline the discipline
     * @param date       the date
     * @param date2      the date 2
     * @return the list
     */
    List<Grade> findByDisciplineAndDatePeriod(final Discipline discipline,
                                              final LocalDate date,
                                              final LocalDate date2);

    /**
     * Find by pupil list.
     *
     * @param pupil the pupil
     * @return the list
     */
    List<Grade> findByPupil(final Pupil pupil);

    /**
     * Find by pupil and date list.
     *
     * @param pupil the pupil
     * @param date  the date
     * @return the list
     */
    List<Grade> findByPupilAndDate(final Pupil pupil, final LocalDate date);

    /**
     * Find by pupil and date period list.
     *
     * @param pupil the pupil
     * @param date  the date
     * @param date2 the date 2
     * @return the list
     */
    List<Grade> findByPupilAndDatePeriod(final Pupil pupil,
                                         final LocalDate date,
                                         final LocalDate date2);

    /**
     * Find by pupil and discipline list.
     *
     * @param pupil      the pupil
     * @param discipline the discipline
     * @return the list
     */
    List<Grade> findByPupilAndDiscipline(final Pupil pupil,
                                         final Discipline discipline);

    /**
     * Find by pupil discipline and date grade.
     *
     * @param pupil      the pupil
     * @param discipline the discipline
     * @param date       the date
     * @return the grade
     */
    Grade findByPupilDisciplineAndDate(final Pupil pupil,
                                          final Discipline discipline,
                                          final LocalDate date);

    /**
     * Find by pupil discipline and date period list.
     *
     * @param pupil      the pupil
     * @param discipline the discipline
     * @param date       the date
     * @param date2      the date 2
     * @return the list
     */
    List<Grade> findByPupilDisciplineAndDatePeriod(final Pupil pupil,
                                                   final Discipline discipline,
                                                   final LocalDate date,
                                                   final LocalDate date2);

    /**
     * Find by class and discipline list.
     *
     * @param aClass     the a class
     * @param discipline the discipline
     * @return the list
     */
    List<Grade> findByClassAndDiscipline(final AClass aClass,
                                         final Discipline discipline);

    /**
     * Find by class discipline and date list.
     *
     * @param aClass     the a class
     * @param discipline the discipline
     * @param date       the date
     * @return the list
     */
    List<Grade> findByClassDisciplineAndDate(final AClass aClass,
                                             final Discipline discipline,
                                             final LocalDate date);

    /**
     * Find by class discipline and date period list.
     *
     * @param aClass     the a class
     * @param discipline the discipline
     * @param date       the date
     * @param date2      the date 2
     * @return the list
     */
    List<Grade> findByClassDisciplineAndDatePeriod(final AClass aClass,
                                                   final Discipline discipline,
                                                   final LocalDate date,
                                                   final LocalDate date2);
}
