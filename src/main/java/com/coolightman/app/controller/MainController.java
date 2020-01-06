package com.coolightman.app.controller;

import com.coolightman.app.repository.AdminRepository;
import com.coolightman.app.repository.ParentRepository;
import com.coolightman.app.repository.PupilRepository;
import com.coolightman.app.repository.TeacherRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final PupilRepository pupilRepository;

    public MainController(final AdminRepository adminRepository,
                          final TeacherRepository teacherRepository,
                          final ParentRepository parentRepository,
                          final PupilRepository pupilRepository) {
        this.adminRepository = adminRepository;
        this.teacherRepository = teacherRepository;
        this.parentRepository = parentRepository;
        this.pupilRepository = pupilRepository;
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
}
