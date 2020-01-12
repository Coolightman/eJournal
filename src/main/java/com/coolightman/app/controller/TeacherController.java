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

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping()
    public String teacherPage() {
        return "teacherPage.html";
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping(value = "/lesson")
    public String lesson(Model model) {
        Teacher teacher = getCurrentTeacher();
        final List<AClass> aClasses = aClassService.findAll();
        final List<Discipline> teacherDisciplines = teacher.getDisciplines();
        model.addAttribute("aClasses", aClasses);
        model.addAttribute("teacherDisciplines", teacherDisciplines);
        return "teacherLessonPage.html";
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping(value = "/changeGrades")
    public String changeGrades(Model model) {
        Teacher teacher = getCurrentTeacher();
        final List<AClass> aClasses = aClassService.findAll();
        final List<Discipline> teacherDisciplines = teacher.getDisciplines();
        model.addAttribute("aClasses", aClasses);
        model.addAttribute("teacherDisciplines", teacherDisciplines);
        return "journalIn.html";
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping(value = "/lessonBody")
    public String lessonBody(Model model,
                             @RequestParam Long discipline,
                             @RequestParam Long aClass) {
        LocalDate date = LocalDate.now();
        createModelForLesson(model, date, aClass, discipline);
        return "lessonPupilsList.html";
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping(value = "/changeGradesBody")
    public String changeGradesBody(Model model,
                                   @RequestParam Long discipline,
                                   @RequestParam Long aClass,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        createModelForLesson(model, date, aClass, discipline);
        return "lessonPupilsList.html";
    }

    private void createModelForLesson(final Model model, final LocalDate date, final Long aClass, final Long discipline) {
        final AClass currentAClass = aClassService.findByID(aClass);
        final Discipline currentDiscipline = disciplineService.findByID(discipline);
        final List<Grade> gradeList = gradeService.findByClassDisciplineAndDate(currentAClass, currentDiscipline, date);
        final List<Pupil> pupilList = pupilService.findByClassName(currentAClass.getName());
        Map<Long, String> gradeMap = pupilList.stream()
                .collect(Collectors.toMap(com.coolightman.app.model.User::getId,
                        pupil -> getGrade(pupil, gradeList)));
        String lessonMsg = getLessonMsg(currentDiscipline, currentAClass, date);

        model.addAttribute("pupils", pupilList);
        model.addAttribute("discipline", currentDiscipline);
        model.addAttribute("grades", gradeMap);
        model.addAttribute("date", date);
        model.addAttribute("lessonMsg", lessonMsg);
    }

    private String getLessonMsg(final Discipline currentDiscipline, final AClass currentAClass, final LocalDate date) {
        return currentDiscipline.getName() + " lesson in " + currentAClass.getName() + " class " + date;
    }

    private String getGrade(final Pupil pupil, List<Grade> gradeList) {
        String gradeStr = "";
        for (final Grade grade : gradeList) {
            if (grade.getPupil().getId().equals(pupil.getId())) {
                gradeStr = gradeStr.concat(grade.getValue().toString());
            }
        }
        return gradeStr;
    }

    private Teacher getCurrentTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String login = user.getUsername();
        return teacherService.findTeacherByLogin(login);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/teachersList")
    public String teachersList(Model model) {
        createTeacherList(model);
        return "listTeachers.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/showUpdate/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("teacher", teacherService.findByID(id));
        model.addAttribute("allDisciplines", disciplineService.findAll());
        return "updateTeacher.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/updateTeacher")
    public String updateTeacher(@Valid @ModelAttribute("teacher") TeacherRequestDto teacherRequestDto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "updateTeacher.html";
        }
        Teacher teacher = getEntity(teacherRequestDto);
        teacherService.update(teacher);
        createTeacherList(model);
        return "listTeachers.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/showSignUp")
    public String showSignUp(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("allDisciplines", disciplineService.findAll());
        return "signUpTeacher.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/signUpTeacher")
    public String signUpTeacher(@Valid @ModelAttribute("teacher") TeacherRequestDto teacherRequestDto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "signUpTeacher.html";
        }
        Teacher teacher = getEntity(teacherRequestDto);
        teacherService.save(teacher);
        createTeacherList(model);
        return "listTeachers.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/deleteTeacher/{id}")
    public String deleteTeacher(@PathVariable("id") long id, Model model) {
        teacherService.deleteByID(id);
        createTeacherList(model);
        return "listTeachers.html";
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
