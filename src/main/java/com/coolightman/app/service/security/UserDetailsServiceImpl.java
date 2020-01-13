package com.coolightman.app.service.security;

import com.coolightman.app.model.Role;
import com.coolightman.app.repository.UserRepository;
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

/**
 * The type User details service.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    /**
     * Instantiates a new User details service.
     *
     * @param userRepository the user repository
     */
    public UserDetailsServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Optional<com.coolightman.app.model.User> user = userRepository.findByLoginIgnoreCase(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role userRole : user.get().getRoles()) {
            GrantedAuthority authority = new SimpleGrantedAuthority(userRole.getName());
            grantedAuthorities.add(authority);
        }

        return new User(user.get().getLogin(), user.get().getPassword(), grantedAuthorities);
    }
}
