package com.coolightman.app.service.security;

import com.coolightman.app.model.Role;
import com.coolightman.app.repository.AdminRepository;
import com.coolightman.app.repository.ParentRepository;
import com.coolightman.app.repository.PupilRepository;
import com.coolightman.app.repository.TeacherRepository;
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
    private AdminRepository adminRepository;
    private ParentRepository parentRepository;
    private TeacherRepository teacherRepository;
    private PupilRepository pupilRepository;

    public UserDetailsServiceImpl(final AdminRepository adminRepository,
                                  final ParentRepository parentRepository,
                                  final TeacherRepository teacherRepository,
                                  final PupilRepository pupilRepository) {
        this.adminRepository = adminRepository;
        this.parentRepository = parentRepository;
        this.teacherRepository = teacherRepository;
        this.pupilRepository = pupilRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        String userLogin = null;
        String userPassword = null;
        List<Role> userRoles = null;

        final Optional<com.coolightman.app.model.User> admin = adminRepository.findByLoginIgnoreCase(login);
        final Optional<com.coolightman.app.model.User> parent = parentRepository.findByLoginIgnoreCase(login);
        final Optional<com.coolightman.app.model.User> teacher = teacherRepository.findByLoginIgnoreCase(login);
        final Optional<com.coolightman.app.model.User> pupil = pupilRepository.findByLoginIgnoreCase(login);
        if (admin.isPresent()) {
            userLogin = admin.get().getLogin();
            userPassword = admin.get().getPassword();
            userRoles = admin.get().getRoles();
        } else if (parent.isPresent()) {
            userLogin = parent.get().getLogin();
            userPassword = parent.get().getPassword();
            userRoles = parent.get().getRoles();
        } else if (teacher.isPresent()) {
            userLogin = teacher.get().getLogin();
            userPassword = teacher.get().getPassword();
            userRoles = teacher.get().getRoles();
        } else if (pupil.isPresent()) {
            userLogin = pupil.get().getLogin();
            userPassword = pupil.get().getPassword();
            userRoles = pupil.get().getRoles();
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
