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
     * @param entity the entity
     * @return the t
     */
    T save(T entity);

    /**
     * Update t.
     *
     * @param entity the entity
     * @return the t
     */
    T update(T entity);

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
    T findByID(Long id);

    /**
     * Delete.
     *
     * @param entity the entity
     */
    void delete(T entity);

    /**
     * Delete by id.
     *
     * @param id the id
     */
    void deleteByID(Long id);
}
