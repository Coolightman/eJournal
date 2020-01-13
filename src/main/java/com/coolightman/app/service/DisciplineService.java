package com.coolightman.app.service;

import com.coolightman.app.model.Discipline;


/**
 * The interface Discipline service.
 */
public interface DisciplineService extends GenericService<Discipline> {
    /**
     * Find by name discipline.
     *
     * @param name the name
     * @return the discipline
     */
    Discipline findByName(final String name);

    /**
     * Exist by name boolean.
     *
     * @param name the name
     * @return the boolean
     */
    boolean existByName(final String name);
}
