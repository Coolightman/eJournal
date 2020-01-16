package com.coolightman.app.service;

import com.coolightman.app.model.Teacher;

import java.util.List;

/**
 * The interface Teacher service.
 */
public interface TeacherService extends GenericService<Teacher> {
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
     * @param name the name
     * @return the list
     */
    List<Teacher> findByDisciplineName(final String name);

    /**
     * Find teacher by login teacher.
     *
     * @param login the login
     * @return the teacher
     */
    Teacher findTeacherByLogin(final String login);

    /**
     * Exists by login boolean.
     *
     * @param login the login
     * @return the boolean
     */
    boolean existsByLogin(final String login);
}
