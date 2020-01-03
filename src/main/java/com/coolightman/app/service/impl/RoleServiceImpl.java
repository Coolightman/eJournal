package com.coolightman.app.service.impl;

import com.coolightman.app.component.LocalizedMessageSource;
import com.coolightman.app.model.Role;
import com.coolightman.app.repository.*;
import com.coolightman.app.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl extends GenericServiceImpl<Role> implements RoleService {
    private Class type = Role.class;

    public RoleServiceImpl(final LocalizedMessageSource localizedMessageSource,
                           final AdminRepository adminRepository,
                           final AClassRepository AClassRepository,
                           final DisciplineRepository disciplineRepository,
                           final GradeRepository gradeRepository,
                           final ParentRepository parentRepository,
                           final PupilRepository pupilRepository,
                           final RoleRepository roleRepository,
                           final TeacherRepository teacherRepository) {
        super(localizedMessageSource, adminRepository,
                AClassRepository, disciplineRepository,
                gradeRepository, parentRepository,
                pupilRepository, roleRepository,
                teacherRepository);
    }

    @Override
    public Role findByName(final String name) {
        return roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> getRuntimeException("error.role.notExist"));
    }

    @Override
    public boolean existByName(final String name) {
        return AClassRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public Role save(final Role role) {
        validate(existByName(role.getName()), "error.role.name.notUnique");
        return super.save(role, type);
    }

    @Override
    public Role update(final Role role) {
        validate(existByName(role.getName()), "error.role.name.notUnique");
        return super.update(role, type);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAllByIdIsNotNullOrderByName();
    }

    @Override
    public Role findByID(final Long id) {
        return super.findByID(id, type);
    }

    @Override
    public void delete(final Role role) {
        super.delete(role, type);
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
