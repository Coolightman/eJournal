package com.coolightman.app.controller;

import com.coolightman.app.dto.request.TeacherRequestDto;
import com.coolightman.app.dto.response.TeacherResponseDto;
import com.coolightman.app.model.*;
import com.coolightman.app.service.*;
import org.dozer.Mapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Teacher controller.
 */
@Controller
@RequestMapping("/teachers")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
public class TeacherController {

    private final Mapper mapper;
    private final TeacherService teacherService;
    private final DisciplineService disciplineService;
    private final AClassService aClassService;
    private final GradeService gradeService;
    private final PupilService pupilService;

    /**
     * Instantiates a new Teacher controller.
     *
     * @param mapper            the mapper
     * @param teacherService    the teacher service
     * @param disciplineService the discipline service
     * @param aClassService     the a class service
     * @param gradeService      the grade service
     * @param pupilService      the pupil service
     */
    public TeacherController(final Mapper mapper,
                             final TeacherService teacherService,
                             final DisciplineService disciplineService,
                             final AClassService aClassService,
                             final GradeService gradeService,
                             final PupilService pupilService) {
        this.mapper = mapper;
        this.teacherService = teacherService;
        this.disciplineService = disciplineService;
        this.aClassService = aClassService;
        this.gradeService = gradeService;
        this.pupilService = pupilService;
    }

    /**
     * Teacher page.
     *
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping()
    public String teacherPage() {
        return "teacherPage.html";
    }

    /**
     * Create lesson page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping(value = "/lesson")
    public String createLesson(Model model) {
        createTeacherActionModel(model);
        return "teacherLessonPage.html";
    }

    /**
     * Create changing grades page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping(value = "/changeGrades")
    public String createChangeGrades(Model model) {
        createTeacherActionModel(model);
        return "journalIn.html";
    }

    /**
     * Lesson page.
     *
     * @param model      the model
     * @param discipline the discipline
     * @param aClass     the a class
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping(value = "/lessonBody")
    public String lessonBody(Model model,
                             @RequestParam Long discipline,
                             @RequestParam Long aClass) {
        LocalDate date = LocalDate.now();
        createModelForLesson(model, date, aClass, discipline);
        return "lessonPupilsList.html";
    }

    /**
     * Change grades page.
     *
     * @param model      the model
     * @param discipline the discipline
     * @param aClass     the a class
     * @param date       the date
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping(value = "/changeGradesBody")
    public String changeGradesBody(Model model,
                                   @RequestParam Long discipline,
                                   @RequestParam Long aClass,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        createModelForLesson(model, date, aClass, discipline);
        return "lessonPupilsList.html";
    }

    /**
     * Teachers list page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/teachersList")
    public String teachersList(Model model) {
        createTeacherList(model);
        return "listTeachers.html";
    }

    /**
     * Show update page.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/showUpdate/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("teacher", teacherService.findByID(id));
        model.addAttribute("allDisciplines", disciplineService.findAll());
        return "updateTeacher.html";
    }

    /**
     * Update teacher.
     *
     * @param teacherRequestDto the teacher request dto
     * @param result            the result
     * @param model             the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/updateTeacher")
    public String updateTeacher(@Valid @ModelAttribute("teacher") TeacherRequestDto teacherRequestDto,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            model.addAttribute("allDisciplines", disciplineService.findAll());
            return "updateTeacher.html";
        }
        return updateAndGetPage(model, getEntity(teacherRequestDto));
    }

    private String updateAndGetPage(final Model model, final Teacher teacher) {
        try {
            teacherService.update(teacher);
            createTeacherList(model);
            return "listTeachers.html";
        } catch (RuntimeException except) {
            model.addAttribute("allDisciplines", disciplineService.findAll());
            model.addAttribute("exceptMsg", except.getMessage());
            return "updateTeacher.html";
        }
    }

    /**
     * Show sign up page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/showSignUp")
    public String showSignUp(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("allDisciplines", disciplineService.findAll());
        return "signUpTeacher.html";
    }

    /**
     * Sign up teacher.
     *
     * @param teacherRequestDto the teacher request dto
     * @param result            the result
     * @param model             the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/signUpTeacher")
    public String signUpTeacher(@Valid @ModelAttribute("teacher") TeacherRequestDto teacherRequestDto,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            model.addAttribute("allDisciplines", disciplineService.findAll());
            return "signUpTeacher.html";
        }
        return saveAndGetPage(model, getEntity(teacherRequestDto));
    }

    private String saveAndGetPage(final Model model, final Teacher teacher) {
        try {
            teacherService.save(teacher);
            createTeacherList(model);
            return "listTeachers.html";
        } catch (RuntimeException except) {
            model.addAttribute("allDisciplines", disciplineService.findAll());
            model.addAttribute("exceptMsg", except.getMessage());
            return "signUpTeacher.html";
        }
    }

    /**
     * Delete teacher.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/deleteTeacher/{id}")
    public String deleteTeacher(@PathVariable("id") long id, Model model) {
        teacherService.deleteByID(id);
        createTeacherList(model);
        return "listTeachers.html";
    }

    private void createTeacherActionModel(final Model model) {
        Teacher teacher = getCurrentTeacher();
        final List<AClass> aClasses = aClassService.findAll();
        final List<Discipline> teacherDisciplines = teacher.getDisciplines();

        model.addAttribute("aClasses", aClasses);
        model.addAttribute("teacherDisciplines", teacherDisciplines);
    }

    private void createModelForLesson(final Model model, final LocalDate date,
                                      final Long aClass, final Long discipline) {

        final AClass currentAClass = aClassService.findByID(aClass);
        final Discipline currentDiscipline = disciplineService.findByID(discipline);
        final List<Pupil> pupilList = pupilService.findByClassName(currentAClass.getName());

//        map (currentPupil.id, grade for current lesson)
        Map<Long, String> gradeMap = pupilList.stream()
                .collect(Collectors.toMap(com.coolightman.app.model.User::getId,
                        pupil -> getGrade(pupil, currentDiscipline, date)));

        createModelMsg(model, currentDiscipline, currentAClass, date);
        model.addAttribute("pupils", pupilList);
        model.addAttribute("discipline", currentDiscipline);
        model.addAttribute("grades", gradeMap);
        model.addAttribute("date", date);
    }

    private void createModelMsg(final Model model, final Discipline currentDiscipline,
                                final AClass currentAClass, final LocalDate date) {

        String lessonMsg = currentDiscipline.getName() + " lesson in " + currentAClass.getName() + " class " + date;
        model.addAttribute("lessonMsg", lessonMsg);
    }

    private String getGrade(final Pupil pupil, final Discipline discipline, final LocalDate date) {
        if (gradeService.existsByPupilAndDisciplineAndDate(pupil, discipline, date)){
            return gradeService.findByPupilDisciplineAndDate(pupil, discipline, date).getValue().toString();
        } else {
            return "";
        }
    }

    private Teacher getCurrentTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String login = user.getUsername();
        return teacherService.findTeacherByLogin(login);
    }

    private void createTeacherList(final Model model) {
        final List<TeacherResponseDto> responseDtos = teacherService.findAll()
                .stream()
                .map(this::setEntity)
                .collect(Collectors.toList());
        model.addAttribute("teachers", responseDtos);
    }

    private Teacher getEntity(TeacherRequestDto requestDto) {
        Teacher teacher = new Teacher();
        teacher.setId(requestDto.getId());
        teacher.setFirstName(requestDto.getFirstName());
        teacher.setSurname(requestDto.getSurname());
        teacher.setLogin(requestDto.getLogin());
        teacher.setPassword(requestDto.getPassword());
        teacher.setDisciplines(requestDto.getDisciplines()
                .stream()
                .map(disciplineService::findByID)
                .collect(Collectors.toList()));
        return teacher;
    }

    private TeacherResponseDto setEntity(Teacher teacher) {
        return mapper.map(teacher, TeacherResponseDto.class);
    }

}
