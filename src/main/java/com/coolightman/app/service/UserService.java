package com.coolightman.app.service;

import com.coolightman.app.model.User;

public interface UserService<T extends User> {

    Long findByLogin(final String login);

    void deleteByLogin(final String login);

    boolean existsByLogin(final String login);
}
