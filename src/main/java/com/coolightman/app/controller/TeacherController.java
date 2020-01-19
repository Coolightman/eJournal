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
import java.util.Optional;
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

    static Map<Long, String> getPupilIdValueMap(final List<Pupil> pupilList, final List<Grade> grades) {
        return pupilList.stream()
                .collect(Collectors.toMap(BaseClass::getId, pupil -> {

                    final Optional<Grade> optionalGrade = grades.stream()
                            .filter(grade -> grade.getPupil().getId().equals(pupil.getId()))
                            .findAny();

                    return optionalGrade.map(grade -> grade.getValue().toString()).orElse("");
                }));
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
    @GetMapping("/lesson")
    public String createLesson(final Model model) {
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
    @GetMapping("/changeGrades")
    public String createChangeGrades(final Model model) {
        createTeacherActionModel(model);
        return "journalIn.html";
    }

    /**
     * Lesson page.
     *
     * @param discipline the discipline
     * @param aClass     the a class
     * @param model      the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping("/lessonBody")
    public String lessonBody(@RequestParam final Long discipline,
                             @RequestParam final Long aClass,
                             final Model model) {

        return createModelForLesson(model, LocalDate.now(), aClass, discipline);
    }

    /**
     * Change grades page.
     *
     * @param discipline the discipline
     * @param aClass     the a class
     * @param date       the date
     * @param model      the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping("/changeGradesBody")
    public String changeGradesBody(@RequestParam final Long discipline,
                                   @RequestParam final Long aClass,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date,
                                   final Model model) {

        return createModelForLesson(model, date, aClass, discipline);
    }

    /**
     * Teachers list page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/teachersList")
    public String teachersList(final Model model) {
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
    public String showUpdate(@PathVariable("id") final Long id, final Model model) {
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
    public String updateTeacher(@Valid @ModelAttribute("teacher") final TeacherRequestDto teacherRequestDto,
                                final BindingResult result,
                                final Model model) {

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
    public String showSignUp(final Model model) {
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
    public String signUpTeacher(@Valid @ModelAttribute("teacher") final TeacherRequestDto teacherRequestDto,
                                final BindingResult result,
                                final Model model) {

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
    public String deleteTeacher(@PathVariable("id") final Long id, final Model model) {
        teacherService.deleteByID(id);
        createTeacherList(model);
        return "listTeachers.html";
    }

    private void createTeacherActionModel(final Model model) {
        final List<AClass> aClasses = aClassService.findAll();
        final List<Discipline> disciplines = getCurrentTeacher().getDisciplines();

        model.addAttribute("aClasses", aClasses);
        model.addAttribute("disciplines", disciplines);
    }

    private String createModelForLesson(final Model model, final LocalDate date,
                                        final Long aClassId, final Long disciplineId) {

        final Discipline discipline = disciplineService.findByID(disciplineId);
        final AClass aClass = aClassService.findByID(aClassId);
        final List<Pupil> pupilList = pupilService.findByClass(aClass);
        final List<Grade> grades = gradeService.findByClassDisciplineAndDate(aClass, discipline, date);

//        create Map<Long pupilId, String GradeValueForCurrentLesson>
        final Map<Long, String> gradeMap = getPupilIdValueMap(pupilList, grades);

        model.addAttribute("pupils", pupilList);
        model.addAttribute("discipline", discipline);
        model.addAttribute("gradeMap", gradeMap);
        model.addAttribute("date", date);
        createModelMsg(model, discipline, aClass, date);
        return "lessonPupilsList.html";
    }

    private void createModelMsg(final Model model, final Discipline discipline,
                                final AClass aClass, final LocalDate date) {

        final String lessonMsg = discipline.getName() + " lesson in " + aClass.getName() + " class " + date;
        model.addAttribute("lessonMsg", lessonMsg);
    }

    private Teacher getCurrentTeacher() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User user = (User) authentication.getPrincipal();
        return teacherService.findByLogin(user.getUsername());
    }

    private void createTeacherList(final Model model) {
        final List<TeacherResponseDto> responseDtos = teacherService.findAll()
                .stream()
                .map(this::setEntity)
                .collect(Collectors.toList());
        model.addAttribute("teachers", responseDtos);
    }

    private Teacher getEntity(final TeacherRequestDto requestDto) {
        final Teacher teacher = new Teacher();
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

    private TeacherResponseDto setEntity(final Teacher teacher) {
        return mapper.map(teacher, TeacherResponseDto.class);
    }

}
