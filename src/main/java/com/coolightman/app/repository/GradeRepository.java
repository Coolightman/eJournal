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
import java.util.Optional;

/**
 * The interface Grade repository.
 */
public interface GradeRepository extends JpaRepository<Grade, Long> {
    /**
     * Find by discipline order by pupil list.
     *
     * @param discipline the discipline
     * @return the list
     */
    List<Grade> findByDisciplineOrderByPupil(final Discipline discipline);

    /**
     * Find by discipline and date order by pupil list.
     *
     * @param discipline the discipline
     * @param date       the date
     * @return the list
     */
    List<Grade> findByDisciplineAndDateOrderByPupil(final Discipline discipline, final LocalDate date);

    /**
     * Find by discipline and date after and date before order by pupil list.
     *
     * @param discipline the discipline
     * @param date       the date
     * @param date2      the date 2
     * @return the list
     */
    List<Grade> findByDisciplineAndDateAfterAndDateBeforeOrderByPupil(final Discipline discipline,
                                                                      final LocalDate date,
                                                                      final LocalDate date2);

    /**
     * Find by pupil order by date list.
     *
     * @param pupil the pupil
     * @return the list
     */
    List<Grade> findByPupilOrderByDate(final Pupil pupil);

    /**
     * Find by pupil and date order by discipline list.
     *
     * @param pupil the pupil
     * @param date  the date
     * @return the list
     */
    List<Grade> findByPupilAndDateOrderByDiscipline(final Pupil pupil, final LocalDate date);

    /**
     * Find by pupil and date after and date before order by date list.
     *
     * @param pupil the pupil
     * @param date  the date
     * @param date2 the date 2
     * @return the list
     */
    List<Grade> findByPupilAndDateAfterAndDateBeforeOrderByDate(final Pupil pupil,
                                                                final LocalDate date,
                                                                final LocalDate date2);

    /**
     * Find by pupil and discipline order by date list.
     *
     * @param pupil      the pupil
     * @param discipline the discipline
     * @return the list
     */
    List<Grade> findByPupilAndDisciplineOrderByDate(final Pupil pupil, final Discipline discipline);

    /**
     * Find by pupil and discipline and date optional.
     *
     * @param pupil      the pupil
     * @param discipline the discipline
     * @param date       the date
     * @return the optional
     */
    Optional<Grade> findByPupilAndDisciplineAndDate(final Pupil pupil,
                                                    final Discipline discipline,
                                                    final LocalDate date);

    /**
     * Find by pupil and discipline and date after and date before order by date list.
     *
     * @param pupil      the pupil
     * @param discipline the discipline
     * @param date       the date
     * @param date2      the date 2
     * @return the list
     */
    List<Grade> findByPupilAndDisciplineAndDateAfterAndDateBeforeOrderByDate(final Pupil pupil,
                                                                             final Discipline discipline,
                                                                             final LocalDate date,
                                                                             final LocalDate date2);

    /**
     * Find by discipline and class list.
     *
     * @param discipline the discipline
     * @param aClass     the a class
     * @return the list
     */
    @Query("SELECT grade FROM Grade grade JOIN grade.pupil grPup " +
            "WHERE grade.discipline = :discipline AND grPup.aClass = :aClass " +
            "ORDER BY grade.date")
    List<Grade> findByDisciplineAndClass(@Param("discipline") final Discipline discipline,
                                         @Param("aClass") final AClass aClass);

    /**
     * Find by discipline and class list.
     *
     * @param discipline the discipline
     * @param aClass     the a class
     * @param date       the date
     * @return the list
     */
    @Query("SELECT grade FROM Grade grade JOIN grade.pupil grPup " +
            "WHERE grade.discipline = :discipline " +
            "AND grPup.aClass = :aClass " +
            "AND grade.date = :date")
    List<Grade> findByDisciplineAndClass(@Param("discipline") final Discipline discipline,
                                         @Param("aClass") final AClass aClass,
                                         @Param("date") final LocalDate date);

    /**
     * Find by discipline and class list.
     *
     * @param discipline the discipline
     * @param aClass     the a class
     * @param date       the date
     * @param date1      the date 1
     * @return the list
     */
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

    /**
     * Exists by pupil and discipline and date boolean.
     *
     * @param pupil      the pupil
     * @param discipline the discipline
     * @param date       the date
     * @return the boolean
     */
    boolean existsByPupilAndDisciplineAndDate(final Pupil pupil,
                                              final Discipline discipline,
                                              final LocalDate date);

    /**
     * Delete by discipline.
     *
     * @param discipline the discipline
     */
    void deleteByDiscipline(final Discipline discipline);

    /**
     * Delete by pupil.
     *
     * @param pupil the pupil
     */
    void deleteByPupil(final Pupil pupil);
}
