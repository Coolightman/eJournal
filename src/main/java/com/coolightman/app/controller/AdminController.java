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

    @PostMapping("/updateAdmin/{id}")
    public String updateAdmin(@PathVariable("id") long id,
                              @Valid @ModelAttribute("admin") AdminRequestDto adminRequestDto,
                              BindingResult result,
                              Model model) {
        Admin admin = getEntity(adminRequestDto);
        admin.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("admin", admin);
            return "updateAdmin.html";
        }
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
        Admin admin = getEntity(adminRequestDto);
        if (result.hasErrors()) {
            model.addAttribute("admin", admin);
            return "signUpAdmin.html";
        }
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
