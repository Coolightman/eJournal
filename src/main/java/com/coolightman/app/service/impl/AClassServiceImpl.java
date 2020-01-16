package com.coolightman.app.service.impl;

import com.coolightman.app.component.LocalizedMessageSource;
import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.repository.*;
import com.coolightman.app.service.AClassService;
import com.coolightman.app.service.PupilService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type A class service.
 */
@Service
@Transactional
public class AClassServiceImpl extends GenericServiceImpl<AClass> implements AClassService {

    private Class type = AClass.class;

    private PupilService pupilService;

    /**
     * Instantiates a new A class service.
     *
     * @param localizedMessageSource the localized message source
     * @param adminRepository        the admin repository
     * @param AClassRepository       the a class repository
     * @param disciplineRepository   the discipline repository
     * @param gradeRepository        the grade repository
     * @param parentRepository       the parent repository
     * @param pupilRepository        the pupil repository
     * @param roleRepository         the role repository
     * @param teacherRepository      the teacher repository
     * @param pupilService           the pupil service
     */
    public AClassServiceImpl(final LocalizedMessageSource localizedMessageSource,
                             final AdminRepository adminRepository,
                             final AClassRepository AClassRepository,
                             final DisciplineRepository disciplineRepository,
                             final GradeRepository gradeRepository,
                             final ParentRepository parentRepository,
                             final PupilRepository pupilRepository,
                             final RoleRepository roleRepository,
                             final TeacherRepository teacherRepository,
                             final PupilService pupilService) {
        super(localizedMessageSource, adminRepository,
                AClassRepository, disciplineRepository,
                gradeRepository, parentRepository,
                pupilRepository, roleRepository,
                teacherRepository);
        this.pupilService = pupilService;
    }

    @Override
    public AClass findByName(final String name) {
        return AClassRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> getRuntimeException("error.class.notExist"));
    }

    @Override
    public boolean existByName(final String name) {
        return AClassRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public AClass save(final AClass aClass) {
        validate(existByName(aClass.getName()), "error.class.name.notUnique");
        return super.save(aClass, type);
    }

    @Override
    public AClass update(final AClass aClass) {
        String currentAClassName = findByID(aClass.getId()).getName();
        if (aClass.getName().equals(currentAClassName)) {
            return super.update(aClass, type);
        } else {
            validate(existByName(aClass.getName()), "error.class.name.notUnique");
            return super.update(aClass, type);
        }
    }

    @Override
    public List<AClass> findAll() {
        return AClassRepository.findAllByOrderByName();
    }

    @Override
    public AClass findByID(final Long id) {
        return super.findByID(id, type);
    }

    @Override
    public void delete(final AClass aClass) {
        super.delete(aClass, type);
    }

    @Override
    public void deleteAll() {
        super.deleteAll(type);
    }

    @Override
    public void deleteByID(final Long id) {
//        Delete class right away if it empty
//        If not - delete first all it Pupil
        final List<Pupil> pupils = pupilService.findByClassName(AClassRepository.getOne(id).getName());
        if (pupils.isEmpty()) {
            super.deleteByID(id, type);
        } else {
            pupilService.findByClassName(AClassRepository.getOne(id)
                    .getName())
                    .forEach(pupil -> pupilService.delete(pupil));
            super.deleteByID(id, type);
        }
    }
}
