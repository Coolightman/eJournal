package com.coolightman.app.model;

import lombok.NonNull;


public abstract class BaseClass {

    @NonNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
