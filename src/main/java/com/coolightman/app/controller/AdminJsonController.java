package com.coolightman.app.controller;

import com.coolightman.app.dto.request.AdminRequestDto;
import com.coolightman.app.dto.response.AdminResponseDto;
import com.coolightman.app.model.Admin;
import com.coolightman.app.service.AdminService;
import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Admin json controller.
 */
@RestController
@RequestMapping("/adminsJson")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminJsonController {
    private final Mapper mapper;
    private final AdminService adminService;

    /**
     * Instantiates a new Admin json controller.
     *
     * @param mapper       the mapper
     * @param adminService the admin service
     */
    public AdminJsonController(final Mapper mapper,
                               final AdminService adminService) {
        this.mapper = mapper;
        this.adminService = adminService;
    }

    /**
     * Find all Admins.
     *
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<List<AdminResponseDto>> findAll() {
        final List<Admin> adminList = adminService.findAll();
        final List<AdminResponseDto> responseDtos = adminList.stream()
                .map(this::setModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    /**
     * Find Admin by id.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping(value = "/findById")
    public ResponseEntity<AdminResponseDto> findById(@RequestParam("id") Long id) {
        final Admin admin = adminService.findByID(id);
        final AdminResponseDto responseDto = setModel(admin);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Find Admin by login.
     *
     * @param login the login
     * @return the response entity
     */
    @GetMapping(value = "/findByLogin")
    public ResponseEntity<AdminResponseDto> findByLogin(@RequestParam("login") String login) {
        final Admin admin = adminService.findAdminByLogin(login);
        final AdminResponseDto responseDto = setModel(admin);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Save Admin.
     *
     * @param requestDto the request dto
     * @return the response entity
     */
    @PostMapping(value = "/save")
    public ResponseEntity<AdminResponseDto> save(@Valid @RequestBody AdminRequestDto requestDto) {
        final Admin admin = getModel(requestDto);
        final Admin saved = adminService.save(admin);
        AdminResponseDto responseDto = setModel(saved);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Update Admin.
     *
     * @param requestDto the request dto
     * @param id         the id
     * @return the response entity
     */
    @PutMapping(value = "/update")
    public ResponseEntity<AdminResponseDto> update(@Valid @RequestBody AdminRequestDto requestDto,
                                                   @RequestParam("id") Long id) {
        final Admin admin = getModel(requestDto);
        admin.setId(id);
        final Admin updated = adminService.update(admin);
        final AdminResponseDto responseDto = setModel(updated);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Delete Admin.
     *
     * @param id the id
     */
    @DeleteMapping(value = "/delete")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@RequestParam("id") Long id) {
        adminService.deleteByID(id);
    }

    private Admin getModel(AdminRequestDto requestDto) {
        return mapper.map(requestDto, Admin.class);
    }

    private AdminResponseDto setModel(Admin admin) {
        return mapper.map(admin, AdminResponseDto.class);
    }
}
