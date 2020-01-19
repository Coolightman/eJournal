package com.coolightman.app.controller;

import com.coolightman.app.security.TokenUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * The type Main controller.
 */
@Controller
public class MainController {
    private final AuthenticationManager authenticationManager;
    private final TokenUtil tokenUtil;
    private final static int COOKIE_AGE_SEC = 60 * 60;

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
     * Login page.
     *
     * @return the string
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    /**
     * Access denied page.
     *
     * @return the string
     */
    @GetMapping("/403")
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
    @GetMapping("/userPage")
    public String userPage() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()).get(0);

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
     * @param model    the model
     * @return the string
     */
    @PostMapping("/authenticate")
    public String authentication(@RequestParam final String username,
                                 @RequestParam final String password,
                                 final HttpServletResponse response,
                                 final Model model) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            final String token = tokenUtil.generateToken(username.trim());
            final Cookie cookie = new Cookie(AUTHORIZATION, token);
            cookie.setMaxAge(COOKIE_AGE_SEC);
            response.addCookie(cookie);
            return "redirect:userPage";
        } catch (AuthenticationException e) {
            model.addAttribute("exceptMsg", e.getMessage());
            return "login.html";
        }
    }
}
