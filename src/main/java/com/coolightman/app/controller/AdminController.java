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

/**
 * The type Admin controller.
 */
@Controller
@RequestMapping("/admins")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final Mapper mapper;
    private final AdminService adminService;

    /**
     * Instantiates a new Admin controller.
     *
     * @param mapper       the mapper
     * @param adminService the admin service
     */
    public AdminController(final Mapper mapper, final AdminService adminService) {
        this.mapper = mapper;
        this.adminService = adminService;
    }

    /**
     * Admin page.
     *
     * @return the string
     */
    @GetMapping()
    public String adminPage() {
        return "adminPage.html";
    }

    /**
     * Admins list page.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping(value = "/adminsList")
    public String adminsList(Model model) {
        createAdminList(model);
        return "listAdmins.html";
    }

    /**
     * Show update page.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/showUpdate/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("admin", adminService.findByID(id));
        return "updateAdmin.html";
    }

    /**
     * Update admin.
     *
     * @param adminRequestDto the admin request dto
     * @param result          the result
     * @param model           the model
     * @return the string
     */
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

    /**
     * Show sign up page.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/showSignUp")
    public String showSignUp(Model model) {
        model.addAttribute("admin", new Admin());
        return "signUpAdmin.html";
    }

    /**
     * Sign up admin.
     *
     * @param adminRequestDto the admin request dto
     * @param result          the result
     * @param model           the model
     * @return the string
     */
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

    /**
     * Delete admin.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
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
