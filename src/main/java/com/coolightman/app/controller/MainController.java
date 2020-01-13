package com.coolightman.app.controller;

import com.coolightman.app.repository.AdminRepository;
import com.coolightman.app.repository.ParentRepository;
import com.coolightman.app.repository.PupilRepository;
import com.coolightman.app.repository.TeacherRepository;
import com.coolightman.app.security.TokenUtil;
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
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final PupilRepository pupilRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenUtil tokenUtil;
    private final int COOKIE_AGE_SEC = 60 * 60;

    /**
     * Instantiates a new Main controller.
     *
     * @param adminRepository       the admin repository
     * @param teacherRepository     the teacher repository
     * @param parentRepository      the parent repository
     * @param pupilRepository       the pupil repository
     * @param authenticationManager the authentication manager
     * @param tokenUtil             the token util
     */
    public MainController(final AdminRepository adminRepository,
                          final TeacherRepository teacherRepository,
                          final ParentRepository parentRepository,
                          final PupilRepository pupilRepository,
                          final AuthenticationManager authenticationManager,
                          final TokenUtil tokenUtil) {
        this.adminRepository = adminRepository;
        this.teacherRepository = teacherRepository;
        this.parentRepository = parentRepository;
        this.pupilRepository = pupilRepository;
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

        if (adminRepository.existsByLoginIgnoreCase(login)) {
            return "redirect:admins";
        } else if (teacherRepository.existsByLoginIgnoreCase(login)) {
            return "redirect:teachers";
        } else if (parentRepository.existsByLoginIgnoreCase(login)) {
            return "redirect:parents";
        } else if (pupilRepository.existsByLoginIgnoreCase(login)) {
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
