package com.coolightman.app.service.impl;

import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.repository.AClassRepository;
import com.coolightman.app.service.AClassService;
import com.coolightman.app.service.PupilService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type A class service.
 */
@Service
@Transactional
public class AClassServiceImpl extends GenericServiceImpl<AClass> implements AClassService {

    private final AClassRepository aClassRepository;
    private final PupilService pupilService;

    /**
     * Instantiates a new A class service.
     *
     * @param repository       the repository
     * @param aClassRepository the a class repository
     * @param pupilService     the pupil service
     */
    public AClassServiceImpl(@Qualifier("AClassRepository") final JpaRepository<AClass, Long> repository,
                             final AClassRepository aClassRepository,
                             final PupilService pupilService) {
        super(repository);
        this.aClassRepository = aClassRepository;
        this.pupilService = pupilService;
    }

    @Override
    public AClass findByName(final String name) {
        return aClassRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("error.class.notExist"));
    }

    @Override
    public boolean existByName(final String name) {
        return aClassRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public AClass save(final AClass aClass) {
        validate(existByName(aClass.getName()), "error.class.name.notUnique");
        return super.save(aClass);
    }

    @Override
    public AClass update(final AClass aClass) {
        final String currentAClassName = findByID(aClass.getId()).getName();

//        do not validate if update with current name
        if (aClass.getName().equals(currentAClassName)) {
            return super.update(aClass);
        } else {
            validate(existByName(aClass.getName()), "error.class.name.notUnique");
            return super.update(aClass);
        }
    }

    @Override
    public List<AClass> findAll() {
        return aClassRepository.findAllByOrderByName();
    }

    @Override
    public void deleteByID(final Long id) {
//        Delete class right away if it empty
//        If not - delete first all it Pupil
        final AClass aClass = aClassRepository.findById(id).orElseThrow(RuntimeException::new);
        final List<Pupil> pupils = pupilService.findByClass(aClass);
        if (pupils.isEmpty()) {
            super.deleteByID(id);
        } else {
            pupils.forEach(pupilService::delete);
            super.deleteByID(id);
        }
    }
}
