package com.coolightman.app.service.impl;

import com.coolightman.app.component.LocalizedMessageSource;
import com.coolightman.app.model.User;
import com.coolightman.app.repository.*;
import com.coolightman.app.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * The type User service.
 *
 * @param <T> the type parameter
 */
@Service
@Transactional
public abstract class UserServiceImpl<T extends User> extends GenericServiceImpl<T> implements UserService<T> {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Instantiates a new User service.
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
     * @param userRepository         the user repository
     * @param passwordEncoder        the password encoder
     */
    public UserServiceImpl(final LocalizedMessageSource localizedMessageSource,
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
                teacherRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Long findByLogin(final String login) {
        Optional<User> user = userRepository.findByLoginIgnoreCase(login);
        if (user.isPresent()) {
            return user.get().getId();
        } else throw getRuntimeException("error.user.notExist");
    }

    @Override
    public void deleteByLogin(final String login) {
        validate(!existsByLogin(login), "error.user.notExist");
        userRepository.deleteById(findByLogin(login));
    }

    @Override
    public boolean existsByLogin(final String login) {
        return userRepository.existsByLoginIgnoreCase(login);
    }

    @Override
    public T save(final T user, Class type) {
        checkLogin(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return super.save(user, type);
    }

    @Override
    public T update(final T user, Class type) {
        String currentLogin = findByID(user.getId()).getLogin();

//        do not validate if update with current login
        if (user.getLogin().equals(currentLogin)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return super.update(user, type);
        } else {
            checkLogin(user);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return super.update(user, type);
        }

    }

    /**
     * Check login.
     *
     * @param user the user
     */
    void checkLogin(final User user) {
        validate(existsByLogin(user.getLogin()), "error.user.login.notUnique");
    }
}
