package com.coolightman.app.service;


import java.util.List;

public interface GenericService<T> {
    T save(final T t, Class type);

    T update(final T t, Class type);

    List<T> findAll(Class type);

    T findByID(final Long id, Class type);

    void delete(final T t, Class type);

    void deleteAll(Class type);

    void deleteByID(final Long id, Class type);

    T save(final T t);

    T update(final T t);

    List<T> findAll();

    T findByID(final Long id);

    void delete(final T t);

    void deleteAll();

    void deleteByID(final Long id);
}
