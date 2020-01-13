package com.coolightman.app.service;

import com.coolightman.app.model.AClass;


/**
 * The interface A class service.
 */
public interface AClassService extends GenericService<AClass> {
    /**
     * Find by name a class.
     *
     * @param name the name
     * @return the a class
     */
    AClass findByName(final String name);

    /**
     * Exist by name boolean.
     *
     * @param name the name
     * @return the boolean
     */
    boolean existByName(final String name);
}
