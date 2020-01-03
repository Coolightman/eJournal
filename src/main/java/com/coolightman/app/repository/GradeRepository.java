package com.coolightman.app.repository;

import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Discipline;
import com.coolightman.app.model.Grade;
import com.coolightman.app.model.Pupil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByDisciplineOrderByPupil(final Discipline discipline);

    List<Grade> findByDisciplineAndDateOrderByPupil(final Discipline discipline, final LocalDate date);

    List<Grade> findByDisciplineAndDateAfterAndDateBeforeOrderByPupil(final Discipline discipline,
                                                                      final LocalDate date,
                                                                      final LocalDate date2);

    List<Grade> findByPupilOrderByDate(final Pupil pupil);

    List<Grade> findByPupilAndDateOrderByDiscipline(final Pupil pupil, final LocalDate date);

    List<Grade> findByPupilAndDateAfterAndDateBeforeOrderByDate(final Pupil pupil,
                                                                final LocalDate date,
                                                                final LocalDate date2);

    List<Grade> findByPupilAndDisciplineOrderByDate(final Pupil pupil, final Discipline discipline);

    List<Grade> findByPupilAndDisciplineAndDate(final Pupil pupil,
                                                final Discipline discipline,
                                                final LocalDate date);

    List<Grade> findByPupilAndDisciplineAndDateAfterAndDateBeforeOrderByDate(final Pupil pupil,
                                                                             final Discipline discipline,
                                                                             final LocalDate date,
                                                                             final LocalDate date2);

    @Query("SELECT grade FROM Grade grade JOIN grade.pupil grPup " +
            "WHERE grade.discipline = :discipline AND grPup.aClass = :aClass " +
            "ORDER BY grade.date")
    List<Grade> findByDisciplineAndClass(@Param("discipline") final Discipline discipline,
                                         @Param("aClass") final AClass aClass);

    @Query("SELECT grade FROM Grade grade JOIN grade.pupil grPup " +
            "WHERE grade.discipline = :discipline " +
            "AND grPup.aClass = :aClass " +
            "AND grade.date = :date")
    List<Grade> findByDisciplineAndClass(@Param("discipline") final Discipline discipline,
                                         @Param("aClass") final AClass aClass,
                                         @Param("date") final LocalDate date);

    @Query("SELECT grade FROM Grade grade JOIN grade.pupil grPup " +
            "WHERE grade.discipline = :discipline " +
            "AND grPup.aClass= :aClass " +
            "AND grade.date >= :date " +
            "AND grade.date <= :date1 " +
            "ORDER BY grade.date")
    List<Grade> findByDisciplineAndClass(@Param("discipline") final Discipline discipline,
                                         @Param("aClass") final AClass aClass,
                                         @Param("date") final LocalDate date,
                                         @Param("date1") final LocalDate date1);

    boolean existsByPupilAndDisciplineAndDate(final Pupil pupil,
                                              final Discipline discipline,
                                              final LocalDate date);
}
