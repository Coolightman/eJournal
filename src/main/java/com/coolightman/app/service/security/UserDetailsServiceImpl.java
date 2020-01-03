package com.coolightman.app.service.security;

import com.coolightman.app.model.*;
import com.coolightman.app.service.AdminService;
import com.coolightman.app.service.ParentService;
import com.coolightman.app.service.PupilService;
import com.coolightman.app.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private AdminService adminService;
    private ParentService parentService;
    private TeacherService teacherService;
    private PupilService pupilService;

    public UserDetailsServiceImpl(final AdminService adminService,
                                  final ParentService parentService,
                                  final TeacherService teacherService,
                                  final PupilService pupilService) {
        this.adminService = adminService;
        this.parentService = parentService;
        this.teacherService = teacherService;
        this.pupilService = pupilService;
    }

    //    переписать под repository
    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        String userLogin = null;
        String userPassword = null;
        List<Role> userRoles = Collections.EMPTY_LIST;

        try {
            final Admin admin = adminService.findAdminByLogin(login);
            userLogin = admin.getLogin();
            userPassword = admin.getPassword();
            userRoles = admin.getRoles();
        } catch (Exception ignored) {
        }

        try {
            final Teacher teacher = teacherService.findTeacherByLogin(login);
            userLogin = teacher.getLogin();
            userPassword = teacher.getPassword();
            userRoles = teacher.getRoles();
        } catch (Exception ignored) {
        }

        try {
            final Parent parent = parentService.findParentByLogin(login);
            userLogin = parent.getLogin();
            userPassword = parent.getPassword();
            userRoles = parent.getRoles();
        } catch (Exception ignored) {
        }

        try {
            final Pupil pupil = pupilService.findPupilByLogin(login);
            userLogin = pupil.getLogin();
            userPassword = pupil.getPassword();
            userRoles = pupil.getRoles();
        } catch (Exception ignored) {
        }

        if (userLogin == null || userPassword == null) {
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
