package com.coolightman.app.repository;

import com.coolightman.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    Optional<User> findByLoginIgnoreCase(final String login);

    boolean existsByLoginIgnoreCase(final String login);

    @Override
    void deleteById(Long id);
}
