package com.coolightman.app.service;


import java.util.List;

/**
 * The interface Generic service.
 *
 * @param <T> the type parameter
 */
public interface GenericService<T> {
    /**
     * Save t.
     *
     * @param t    the t
     * @param type the type
     * @return the t
     */
    T save(final T t, Class type);

    /**
     * Update t.
     *
     * @param t    the t
     * @param type the type
     * @return the t
     */
    T update(final T t, Class type);

    /**
     * Find all list.
     *
     * @param type the type
     * @return the list
     */
    List<T> findAll(Class type);

    /**
     * Find by id t.
     *
     * @param id   the id
     * @param type the type
     * @return the t
     */
    T findByID(final Long id, Class type);

    /**
     * Delete.
     *
     * @param t    the t
     * @param type the type
     */
    void delete(final T t, Class type);

    /**
     * Delete all.
     *
     * @param type the type
     */
    void deleteAll(Class type);

    /**
     * Delete by id.
     *
     * @param id   the id
     * @param type the type
     */
    void deleteByID(final Long id, Class type);

    /**
     * Save t.
     *
     * @param t the t
     * @return the t
     */
    T save(final T t);

    /**
     * Update t.
     *
     * @param t the t
     * @return the t
     */
    T update(final T t);

    /**
     * Find all list.
     *
     * @return the list
     */
    List<T> findAll();

    /**
     * Find by id t.
     *
     * @param id the id
     * @return the t
     */
    T findByID(final Long id);

    /**
     * Delete.
     *
     * @param t the t
     */
    void delete(final T t);

    /**
     * Delete all.
     */
    void deleteAll();

    /**
     * Delete by id.
     *
     * @param id the id
     */
    void deleteByID(final Long id);
}
