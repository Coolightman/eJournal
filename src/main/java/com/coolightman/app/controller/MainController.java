package com.coolightman.app.controller;

import com.coolightman.app.repository.AdminRepository;
import com.coolightman.app.repository.ParentRepository;
import com.coolightman.app.repository.PupilRepository;
import com.coolightman.app.repository.TeacherRepository;
import com.coolightman.app.security.TokenUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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

@Controller
public class MainController {
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final PupilRepository pupilRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenUtil tokenUtil;

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

    @GetMapping(value = {"/", "/index"})
    public String index() {
        return "index.html";
    }

    @GetMapping(value = {"/login"})
    public String authentication() {
        return "authentication.html";
    }

    @GetMapping(value = {"/403"})
    public String accessDenies() {
        return "403error.html";
    }

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

    @PostMapping(value = "/authenticate")
    public String createAuthenticationToken(@RequestParam String username,
                                            @RequestParam String password,
                                            HttpServletResponse response) throws Exception {
        authenticate(username.trim(), password.trim());

        final String token = tokenUtil.generateToken(username.trim());
        Cookie cookie = new Cookie(AUTHORIZATION, token);
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
        return "redirect:userPage";
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
