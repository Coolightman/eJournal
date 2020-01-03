package com.coolightman.app.service;


import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Discipline;
import com.coolightman.app.model.Grade;
import com.coolightman.app.model.Pupil;

import java.time.LocalDate;
import java.util.List;

public interface GradeService extends GenericService<Grade> {
    List<Grade> findByDiscipline(final Discipline discipline);

    List<Grade> findByDisciplineAndDate(final Discipline discipline, final LocalDate date);

    List<Grade> findByDisciplineAndDatePeriod(final Discipline discipline,
                                              final LocalDate date,
                                              final LocalDate date2);

    List<Grade> findByPupil(final Pupil pupil);

    List<Grade> findByPupilAndDate(final Pupil pupil, final LocalDate date);

    List<Grade> findByPupilAndDatePeriod(final Pupil pupil,
                                         final LocalDate date,
                                         final LocalDate date2);

    List<Grade> findByPupilAndDiscipline(final Pupil pupil,
                                         final Discipline discipline);

    List<Grade> findByPupilDisciplineAndDate(final Pupil pupil,
                                             final Discipline discipline,
                                             final LocalDate date);

    List<Grade> findByPupilDisciplineAndDatePeriod(final Pupil pupil,
                                                   final Discipline discipline,
                                                   final LocalDate date,
                                                   final LocalDate date2);

    List<Grade> findByClassAndDiscipline(final AClass aClass,
                                         final Discipline discipline);

    List<Grade> findByClassDisciplineAndDate(final AClass aClass,
                                             final Discipline discipline,
                                             final LocalDate date);

    List<Grade> findByClassDisciplineAndDatePeriod(final AClass aClass,
                                                   final Discipline discipline,
                                                   final LocalDate date,
                                                   final LocalDate date2);
}
