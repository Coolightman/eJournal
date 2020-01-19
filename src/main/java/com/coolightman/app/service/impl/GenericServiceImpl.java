package com.coolightman.app.service.impl;

import com.coolightman.app.model.BaseClass;
import com.coolightman.app.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
 * The type Generic service.
 *
 * @param <T> the type parameter
 */
@Service
@Transactional
abstract class GenericServiceImpl<T extends BaseClass> implements GenericService<T> {

    /**
     * The Repository.
     */
    private final JpaRepository<T, Long> repository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Instantiates a new Generic service.
     *
     * @param repository the repository
     */
    public GenericServiceImpl(final JpaRepository<T, Long> repository) {
        this.repository = repository;
    }


    @Override
    public T save(final T entity) {
        validate(entity.getId() != null, "error.entity.hasId");
        return repository.saveAndFlush(entity);
    }

    @Override
    public T update(final T entity) {
        validate(entity.getId() == null, "error.entity.notHasId");
        validate(!repository.findById(entity.getId()).isPresent(), "error.entity.notExist");
        return repository.saveAndFlush(entity);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T findByID(final Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("error.entity.notExist"));
    }

    @Override
    public void delete(final T entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteByID(final Long id) {
        validate(!repository.findById(id).isPresent(), "error.entity.notExist");
        repository.deleteById(id);
    }

    /**
     * Validate.
     *
     * @param expression   the expression
     * @param errorMessage the error message
     */
    public void validate(final boolean expression, final String errorMessage) {
        if (expression) {
            throw new RuntimeException(messageSource.getMessage(errorMessage, new Object[]{}, Locale.getDefault()));
        }
    }
}

