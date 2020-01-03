package com.coolightman.app.service;

import com.coolightman.app.model.Discipline;


public interface DisciplineService extends GenericService<Discipline> {
    Discipline findByName(final String name);

    boolean existByName(final String name);
}
