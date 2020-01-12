package com.coolightman.app.controller;

import com.coolightman.app.dto.request.AdminRequestDto;
import com.coolightman.app.dto.response.AdminResponseDto;
import com.coolightman.app.model.Admin;
import com.coolightman.app.service.AdminService;
import org.dozer.Mapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admins")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final Mapper mapper;
    private final AdminService adminService;

    public AdminController(final Mapper mapper, final AdminService adminService) {
        this.mapper = mapper;
        this.adminService = adminService;
    }

    @GetMapping()
    public String adminPage() {
        return "adminPage.html";
    }

    @GetMapping(value = "/adminsList")
    public String adminsList(Model model) {
        createAdminList(model);
        return "listAdmins.html";
    }

    @GetMapping("/showUpdate/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("admin", adminService.findByID(id));
        return "updateAdmin.html";
    }

    @PostMapping("/updateAdmin")
    public String updateAdmin(@Valid @ModelAttribute("admin") AdminRequestDto adminRequestDto,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "updateAdmin.html";
        }
        Admin admin = getEntity(adminRequestDto);
        adminService.update(admin);
        createAdminList(model);
        return "listAdmins.html";
    }

    @GetMapping("/showSignUp")
    public String showSignUp(Model model) {
        model.addAttribute("admin", new Admin());
        return "signUpAdmin.html";
    }

    @PostMapping("/signUpAdmin")
    public String signUpAdmin(@Valid @ModelAttribute("admin") AdminRequestDto adminRequestDto,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "signUpAdmin.html";
        }
        Admin admin = getEntity(adminRequestDto);
        adminService.save(admin);
        createAdminList(model);
        return "listAdmins.html";
    }

    @GetMapping("/deleteAdmin/{id}")
    public String deleteAdmin(@PathVariable("id") long id, Model model) {
        adminService.deleteByID(id);
        createAdminList(model);
        return "listAdmins.html";
    }

    private void createAdminList(final Model model) {
        final List<AdminResponseDto> adminResponseDtos = adminService.findAll()
                .stream()
                .map(this::setEntity)
                .collect(Collectors.toList());
        model.addAttribute("admins", adminResponseDtos);
    }

    private Admin getEntity(AdminRequestDto requestDto) {
        return mapper.map(requestDto, Admin.class);
    }

    private AdminResponseDto setEntity(Admin admin) {
        return mapper.map(admin, AdminResponseDto.class);
    }
}
