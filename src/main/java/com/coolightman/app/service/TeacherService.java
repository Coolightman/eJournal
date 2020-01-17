package com.coolightman.app.service;

import com.coolightman.app.model.Discipline;
import com.coolightman.app.model.Teacher;

import java.util.List;

/**
 * The interface Teacher service.
 */
public interface TeacherService extends UserService<Teacher> {
    /**
     * Find by name list.
     *
     * @param firstName the first name
     * @param surname   the surname
     * @return the list
     */
    List<Teacher> findByName(final String firstName, final String surname);

    /**
     * Find by discipline name list.
     *
     * @param discipline the discipline
     * @return the list
     */
    List<Teacher> findByDiscipline(final Discipline discipline);
}
