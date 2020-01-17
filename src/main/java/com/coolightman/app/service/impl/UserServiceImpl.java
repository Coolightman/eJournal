package com.coolightman.app.service.impl;

import com.coolightman.app.model.User;
import com.coolightman.app.repository.UserRepository;
import com.coolightman.app.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type User service.
 *
 * @param <T> the type parameter
 */
@Service
@Transactional
public abstract class UserServiceImpl<T extends User> extends GenericServiceImpl<T> implements UserService<T> {

    private final UserRepository<T> userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Instantiates a new User service.
     *
     * @param repository      the repository
     * @param userRepository  the user repository
     * @param passwordEncoder the password encoder
     */
    public UserServiceImpl(final JpaRepository<T, Long> repository,
                           final UserRepository<T> userRepository,
                           final BCryptPasswordEncoder passwordEncoder) {
        super(repository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public T findByLogin(final String login) {
        return (T) userRepository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new RuntimeException("error.user.notExist"));
    }

    @Override
    public boolean existsByLogin(final String login) {
        return userRepository.existsByLoginIgnoreCase(login);
    }

    @Override
    public T save(final T user) {
        checkLogin(user);
        encodeUserPassword(user);
        return super.save(user);
    }

    @Override
    public T update(final T user) {
        String currentLogin = findByID(user.getId()).getLogin();

//        do not validate if update with current login
        if (user.getLogin().equals(currentLogin)) {
            encodeUserPassword(user);
            return super.update(user);
        } else {
            checkLogin(user);
            encodeUserPassword(user);
            return super.update(user);
        }

    }

    private void encodeUserPassword(final T user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private void checkLogin(final T user) {
        validate(existsByLogin(user.getLogin()), "error.user.login.notUnique");
    }
}
