package com.coolightman.app.service.impl;

import com.coolightman.app.component.LocalizedMessageSource;
import com.coolightman.app.model.Admin;
import com.coolightman.app.model.Role;
import com.coolightman.app.repository.*;
import com.coolightman.app.service.AdminService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminServiceImpl extends UserServiceImpl<Admin> implements AdminService {

    private Class<Admin> type = Admin.class;

    public AdminServiceImpl(final LocalizedMessageSource localizedMessageSource,
                            final AdminRepository adminRepository,
                            final AClassRepository AClassRepository,
                            final DisciplineRepository disciplineRepository,
                            final GradeRepository gradeRepository,
                            final ParentRepository parentRepository,
                            final PupilRepository pupilRepository,
                            final RoleRepository roleRepository,
                            final TeacherRepository teacherRepository,
                            final UserRepository userRepository,
                            final BCryptPasswordEncoder passwordEncoder) {
        super(localizedMessageSource, adminRepository,
                AClassRepository, disciplineRepository,
                gradeRepository, parentRepository,
                pupilRepository, roleRepository,
                teacherRepository, userRepository,
                passwordEncoder);
    }

    @Override
    public Admin findAdminByLogin(final String login) {
        Long ID = super.findByLogin(login);
        return super.findByID(ID, type);
    }

    @Override
    public Admin save(final Admin admin) {
        Role role = roleRepository.findByNameIgnoreCase("ROLE_ADMIN").get();
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        admin.setRoles(roles);
        return super.save(admin, type);
    }

    @Override
    public Admin update(final Admin admin) {
        Role role = roleRepository.findByNameIgnoreCase("ROLE_ADMIN").get();
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        admin.setRoles(roles);
        return super.update(admin, type);
    }

    @Override
    public List<Admin> findAll() {
        return super.findAll(type);
    }

    @Override
    public Admin findByID(final Long id) {
        return super.findByID(id, type);
    }

    @Override
    public void delete(final Admin admin) {
        super.delete(admin, type);
    }

    @Override
    public void deleteAll() {
        super.deleteAll(type);
    }

    @Override
    public void deleteByID(final Long id) {
        super.deleteByID(id, type);
    }
}
