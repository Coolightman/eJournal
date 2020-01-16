package com.coolightman.app.controller;

import com.coolightman.app.security.TokenUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * The type Main controller.
 */
@Controller
public class MainController {
    private final AuthenticationManager authenticationManager;
    private final TokenUtil tokenUtil;
    private final int COOKIE_AGE_SEC = 60 * 60;

    /**
     * Instantiates a new Main controller.
     *
     * @param authenticationManager the authentication manager
     * @param tokenUtil             the token util
     */
    public MainController(final AuthenticationManager authenticationManager,
                          final TokenUtil tokenUtil) {

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
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        final String role = roles.get(0);
        switch (role) {
            case "ROLE_ADMIN":
                return "redirect:admins";
            case "ROLE_TEACHER":
                return "redirect:teachers";
            case "ROLE_PARENT":
                return "redirect:parents";
            case "ROLE_PUPIL":
                return "redirect:pupils";
            default:
                throw new RuntimeException();
        }
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
