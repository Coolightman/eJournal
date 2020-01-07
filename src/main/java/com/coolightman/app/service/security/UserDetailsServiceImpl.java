package com.coolightman.app.service.security;

import com.coolightman.app.model.Role;
import com.coolightman.app.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private UserRepository userRepository;

    public UserDetailsServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        String userLogin = null;
        String userPassword = null;
        List<Role> userRoles = null;

        final Optional<com.coolightman.app.model.User> user = userRepository.findByLoginIgnoreCase(login);
        if (user.isPresent()){
            userLogin = user.get().getLogin();
            userPassword = user.get().getPassword();
            userRoles = user.get().getRoles();
        } else {
            throw new UsernameNotFoundException("User " + login + " was not found in the database");
        }

        LOGGER.info(userLogin + "/" + userPassword + "/" + userRoles);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (Role userRole : userRoles) {
            GrantedAuthority authority = new SimpleGrantedAuthority(userRole.getName());
            grantedAuthorities.add(authority);
        }

        return new User(userLogin, userPassword, grantedAuthorities);
    }
}
