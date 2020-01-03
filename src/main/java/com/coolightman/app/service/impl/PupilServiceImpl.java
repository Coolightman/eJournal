package com.coolightman.app.service.impl;

import com.coolightman.app.component.LocalizedMessageSource;
import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.model.Role;
import com.coolightman.app.repository.*;
import com.coolightman.app.service.PupilService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PupilServiceImpl extends UserServiceImpl<Pupil> implements PupilService {

    private Class<Pupil> type = Pupil.class;

    public PupilServiceImpl(final LocalizedMessageSource localizedMessageSource,
                            final AdminRepository adminRepository,
                            final AClassRepository AClassRepository,
                            final DisciplineRepository disciplineRepository,
                            final GradeRepository gradeRepository,
                            final ParentRepository parentRepository,
                            final PupilRepository pupilRepository,
                            final RoleRepository roleRepository,
                            final TeacherRepository teacherRepository,
                            final UserRepository userRepository) {
        super(localizedMessageSource, adminRepository,
                AClassRepository, disciplineRepository,
                gradeRepository, parentRepository,
                pupilRepository, roleRepository,
                teacherRepository, userRepository);
    }

    @Override
    public List<Pupil> findByName(final String firstName, final String surname) {
        final List<Pupil> pupils = pupilRepository
                .findByFirstNameIgnoreCaseAndSurnameIgnoreCaseOrderByDob(firstName, surname);
        validate(pupils.size() == 0, "error.pupil.notExist");
        return pupils;
    }

    @Override
    public Pupil findByNameAndDob(final String firstName, final String surname, final LocalDate date) {
        return pupilRepository.findByFirstNameIgnoreCaseAndSurnameIgnoreCaseAndDob(firstName, surname, date)
                .orElseThrow(() -> getRuntimeException("error.pupil.notExist"));
    }

    @Override
    public List<Pupil> findByClassName(final String name) {
        AClassRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> getRuntimeException("error.class.notExist"));
        final List<Pupil> pupils = pupilRepository.findByClassName(name);
//        validate(pupils.size() == 0, "error.pupil.notExist");
        return pupils;
    }

    @Override
    public Pupil findPupilByLogin(final String login) {
        Long ID = super.findByLogin(login);
        return findByID(ID);
    }

    @Override
    public Pupil save(final Pupil pupil) {
        Role role = roleRepository.findByNameIgnoreCase("ROLE_PUPIL").get();
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        pupil.setRoles(roles);
        return super.save(pupil, type);
    }

    @Override
    public Pupil update(final Pupil pupil) {
        return super.update(pupil, type);
    }

    @Override
    public List<Pupil> findAll() {
        return super.findAll(type);
    }

    @Override
    public Pupil findByID(final Long id) {
        return super.findByID(id, type);
    }

//    по всем delete!! В связи с наличием связи pupil-->parent при наличии parent надо удалять через него

    @Override
    public void delete(final Pupil pupil) {
        deleteByID(pupil.getId());
    }

    @Override
    public void deleteAll() {
//        удаляем всех parents со связями с pupils
        pupilRepository.findAll()
                .forEach(pupil -> parentRepository.findByPupil(pupil)
                        .ifPresent(parentRepository::delete));
//        удаляем тех что остались
        pupilRepository.deleteAll();
    }

    @Override
    public void deleteByID(final Long id) {
        final Pupil pupil = findByID(id);
        final Optional<Parent> parentOpt = parentRepository.findByPupil(pupil);
        if (parentOpt.isPresent()) {
            parentOpt.ifPresent(parentRepository::delete);
        } else {
            pupilRepository.deleteById(id);
        }
    }
}