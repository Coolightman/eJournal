package com.coolightman.app.controller;

import com.coolightman.app.security.TokenUtil;
import com.coolightman.app.service.AdminService;
import com.coolightman.app.service.ParentService;
import com.coolightman.app.service.PupilService;
import com.coolightman.app.service.TeacherService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * The type Main controller.
 */
@Controller
public class MainController {
    private final AdminService adminService;
    private final TeacherService teacherService;
    private final ParentService parentService;
    private final PupilService pupilService;
    private final AuthenticationManager authenticationManager;
    private final TokenUtil tokenUtil;
    private final int COOKIE_AGE_SEC = 60 * 60;

    /**
     * Instantiates a new Main controller.
     *
     * @param adminService       the admin service
     * @param teacherService     the teacher service
     * @param parentService      the parent service
     * @param pupilService       the pupil service
     * @param authenticationManager the authentication manager
     * @param tokenUtil             the token util
     */
    public MainController(final AdminService adminService,
                          final TeacherService teacherService,
                          final ParentService parentService,
                          final PupilService pupilService,
                          final AuthenticationManager authenticationManager,
                          final TokenUtil tokenUtil) {
        this.adminService = adminService;
        this.teacherService = teacherService;
        this.parentService = parentService;
        this.pupilService = pupilService;
        this.authenticationManager = authenticationManager;
        this.tokenUtil = tokenUtil;
    }

    /**
     * Index page.
     *
     * @return the string
     */
    @GetMapping(value = {"/", "/index"})
    public String index() {
        return "index.html";
    }

    /**
     * Show authentication page.
     *
     * @return the string
     */
    @GetMapping(value = {"/login"})
    public String showAuthentication() {
        return "authentication.html";
    }

    /**
     * Access denied page.
     *
     * @return the string
     */
    @GetMapping(value = {"/403"})
    public String accessDenied() {
        return "403error.html";
    }

    /**
     * User page.
     * Depending on the role sends to the desired page
     *
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER') " +
            "or hasRole('ROLE_PARENT') or hasRole('ROLE_PUPIL')")
    @GetMapping(value = {"/userPage"})
    public String userPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String login = user.getUsername();

        if (adminService.existsByLogin(login)) {
            return "redirect:admins";
        } else if (teacherService.existsByLogin(login)) {
            return "redirect:teachers";
        } else if (parentService.existsByLogin(login)) {
            return "redirect:parents";
        } else if (pupilService.existsByLogin(login)) {
            return "redirect:pupils";
        } else throw new RuntimeException();
    }

    /**
     * Authentication.
     *
     * @param username the username
     * @param password the password
     * @param response the response
     * @return the string
     */
    @PostMapping(value = "/authenticate")
    public String authentication(@RequestParam String username,
                                 @RequestParam String password,
                                 HttpServletResponse response) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        final String token = tokenUtil.generateToken(username.trim());

        Cookie cookie = new Cookie(AUTHORIZATION, token);
        cookie.setMaxAge(COOKIE_AGE_SEC);
        response.addCookie(cookie);
        return "redirect:userPage";
    }
}
