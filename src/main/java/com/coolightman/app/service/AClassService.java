package com.coolightman.app.service;

import com.coolightman.app.model.AClass;


public interface AClassService extends GenericService<AClass> {
    AClass findByName(final String name);

    boolean existByName(final String name);
}
