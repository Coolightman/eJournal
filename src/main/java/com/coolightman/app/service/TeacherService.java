package com.coolightman.app.service;

import com.coolightman.app.model.Teacher;

import java.util.List;

public interface TeacherService extends GenericService<Teacher> {
    List<Teacher> findByName(final String firstName, final String surname);

    List<Teacher> findByDisciplineName(final String name);

    Teacher findTeacherByLogin(final String login);
}
