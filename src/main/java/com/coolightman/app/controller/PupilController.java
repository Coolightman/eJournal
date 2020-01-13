package com.coolightman.app.controller;

import com.coolightman.app.dto.request.PupilRequestDto;
import com.coolightman.app.dto.response.GradeResponseDto;
import com.coolightman.app.dto.response.PupilResponseDto;
import com.coolightman.app.model.Grade;
import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.repository.ParentRepository;
import com.coolightman.app.repository.PupilRepository;
import com.coolightman.app.service.AClassService;
import com.coolightman.app.service.GradeService;
import com.coolightman.app.service.ParentService;
import com.coolightman.app.service.PupilService;
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
import java.util.stream.Collectors;

/**
 * The type Pupil controller.
 */
@Controller
@RequestMapping("/pupils")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PUPIL')")
public class PupilController {

    private final PupilService pupilService;
    private final PupilRepository pupilRepository;
    private final ParentService parentService;
    private final ParentRepository parentRepository;
    private final AClassService aClassService;
    private final GradeService gradeService;

    /**
     * Instantiates a new Pupil controller.
     *
     * @param pupilService     the pupil service
     * @param pupilRepository  the pupil repository
     * @param parentService    the parent service
     * @param parentRepository the parent repository
     * @param aClassService    the a class service
     * @param gradeService     the grade service
     */
    public PupilController(final PupilService pupilService,
                           final PupilRepository pupilRepository,
                           final ParentService parentService,
                           final ParentRepository parentRepository,
                           final AClassService aClassService,
                           final GradeService gradeService) {
        this.pupilService = pupilService;
        this.pupilRepository = pupilRepository;
        this.parentService = parentService;
        this.parentRepository = parentRepository;
        this.aClassService = aClassService;
        this.gradeService = gradeService;
    }

    /**
     * Pupil page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping()
    public String pupilPage(Model model) {
        model.addAttribute("userText", createPageText());
        return "pupilAndParentPage.html";
    }

    /**
     * Grades for today page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping(value = "/gradesForToday")
    public String gradesForToday(Model model) {
        LocalDate date = LocalDate.now();
        createModel(model, gradeService.findByPupilAndDate(getCurrentPupil(), date));
        return "listGrades.html";
    }


    /**
     * Grades for yesterday page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping(value = "/gradesForYesterday")
    public String gradesForYesterday(Model model) {
        LocalDate date = LocalDate.now().minusDays(1L);
        createModel(model, gradeService.findByPupilAndDate(getCurrentPupil(), date));
        return "listGrades.html";
    }

    /**
     * Grades for week page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping(value = "/gradesForWeek")
    public String gradesForWeek(Model model) {
        LocalDate lastDay = LocalDate.now().plusDays(1L);
        LocalDate firstDay = lastDay.minusDays(7L);
        createModel(model, gradeService.findByPupilAndDatePeriod(getCurrentPupil(), firstDay, lastDay));
        return "listGrades.html";
    }

    /**
     * Grades for month page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping(value = "/gradesForMonth")
    public String gradesForMonth(Model model) {
        LocalDate lastDay = LocalDate.now().plusDays(1L);
        LocalDate firstDay = lastDay.minusDays(30L);
        createModel(model, gradeService.findByPupilAndDatePeriod(getCurrentPupil(), firstDay, lastDay));
        return "listGrades.html";
    }

    /**
     * Grades all page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping(value = "/gradesAll")
    public String gradesAll(Model model) {
        createModel(model, gradeService.findByPupil(getCurrentPupil()));
        return "listGrades.html";
    }

    /**
     * Grades for day page.
     *
     * @param day   the day
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @PostMapping(value = "/gradesForDay")
    public String gradesForDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
                               Model model) {
        createModel(model, gradeService.findByPupilAndDate(getCurrentPupil(), day));
        return "listGrades.html";
    }

    /**
     * Grades for period page.
     *
     * @param firstDay the first day
     * @param lastDay  the last day
     * @param model    the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @PostMapping(value = "/gradesForPeriod")
    public String gradesForPeriod(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate firstDay,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDay,
                                  Model model) {
        createModel(model, gradeService.findByPupilAndDatePeriod(
                getCurrentPupil(), firstDay.minusDays(1L), lastDay.plusDays(1L)));
        return "listGrades.html";
    }

    /**
     * Choose pupils class page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/choosePupilsClass")
    public String choosePupilsClass(Model model) {
        model.addAttribute("classes", aClassService.findAll());
        return "choosePupilsClass.html";
    }

    /**
     * Pupils list by class page.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/pupilsListByClass/{id}")
    public String pupilsListByClass(@PathVariable("id") long id, Model model) {
        createPupilsList(model, id);
        return "listPupilsByClass.html";
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
        final Pupil pupil = pupilService.findByID(id);
        Parent parent = getParent(pupil);
        model.addAttribute("pupil", pupil);
        model.addAttribute("classes", aClassService.findAll());
        model.addAttribute("parent", parent);
        return "updatePupil.html";
    }

    private Parent getParent(final Pupil pupil) {
        Parent parent = null;
//        блок try-catch для прокидывания except отсутствия parent
        try {
            parent = parentService.findByPupil(pupil);
        } catch (Exception ignored) {

        }
        return parent;
    }

    /**
     * Update pupil.
     *
     * @param pupilRequestDto the pupil request dto
     * @param result          the result
     * @param model           the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/updatePupil")
    public String updatePupil(@Valid @ModelAttribute("pupil") PupilRequestDto pupilRequestDto,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            return "updatePupil.html";
        }
        Pupil pupil = getEntity(pupilRequestDto);
        pupilService.update(pupil);
        createPupilsList(model, pupil.getAClass().getId());
        return "listPupilsByClass.html";
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
        model.addAttribute("pupil", new Pupil());
        model.addAttribute("classes", aClassService.findAll());
        return "signUpPupil.html";
    }

    /**
     * Sign up pupil.
     *
     * @param pupilRequestDto the pupil request dto
     * @param result          the result
     * @param model           the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/signUpPupil")
    public String signUpPupil(@Valid @ModelAttribute("pupil") PupilRequestDto pupilRequestDto,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            return "signUpPupil.html";
        }
        Pupil pupil = getEntity(pupilRequestDto);
        pupilService.save(pupil);
        createPupilsList(model, pupil.getAClass().getId());
        return "listPupilsByClass.html";
    }

    /**
     * Delete pupil.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/deletePupil/{id}")
    public String deletePupil(@PathVariable("id") long id, Model model) {
        Long classId = pupilService.findByID(id).getAClass().getId();
        pupilService.deleteByID(id);
        createPupilsList(model, classId);
        return "listPupilsByClass.html";
    }

    private String createPageText() {
        Pupil pupil = getCurrentPupil();
        return "Choose grade period of " + pupil.getSurname() + " " + pupil.getFirstName();
    }

    private void createModel(final Model model, final List<Grade> grades) {
        addGradesInModel(model, grades);
        addPupilText(model);
    }

    private void addGradesInModel(final Model model, final List<Grade> grades) {
        final List<GradeResponseDto> responseDtos = getGradeResponseDtos(grades);
        model.addAttribute("grades", responseDtos);
    }

    private void addPupilText(final Model model) {
        Pupil pupil = getCurrentPupil();
        String firstName = pupil.getFirstName();
        String surname = pupil.getSurname();
        String aClassName = pupil.getAClass().getName();
        String pupilText = "Grades of pupil " + aClassName + " class \n" + surname + " " + firstName;
        model.addAttribute("pupilText", pupilText);
    }

    private List<GradeResponseDto> getGradeResponseDtos(final List<Grade> grades) {
        return grades.stream()
                .map(this::getGradeResponseDto)
                .collect(Collectors.toList());
    }

    private Pupil getCurrentPupil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String login = user.getUsername();

        if (pupilRepository.existsByLoginIgnoreCase(login)) {
            return pupilService.findPupilByLogin(login);
        } else if (parentRepository.existsByLoginIgnoreCase(login)) {
            return parentService.findParentByLogin(login).getPupil();
        } else throw new RuntimeException();
    }

    private void createPupilsList(final Model model, final Long aClassId) {
        final String className = aClassService.findByID(aClassId).getName();
        final List<PupilResponseDto> responseDtos = pupilService.findByClassName(className)
                .stream()
                .map(this::setEntity)
                .collect(Collectors.toList());
        model.addAttribute("pupils", responseDtos);
        model.addAttribute("className", className);
    }

    private Pupil getEntity(PupilRequestDto requestDto) {
        Pupil pupil = new Pupil();
        pupil.setLogin(requestDto.getLogin());
        pupil.setPassword(requestDto.getPassword());
        pupil.setFirstName(requestDto.getFirstName());
        pupil.setSurname(requestDto.getSurname());
        pupil.setAClass(aClassService.findByID(requestDto.getAClass()));
        pupil.setDob(requestDto.getDob());
        return pupil;
    }

    private PupilResponseDto setEntity(Pupil pupil) {
        return getPupilResponseDto(pupil);
    }

    /**
     * Gets pupil response dto.
     *
     * @param pupil the pupil
     * @return the pupil response dto
     */
    static PupilResponseDto getPupilResponseDto(final Pupil pupil) {
        PupilResponseDto responseDto = new PupilResponseDto();
        responseDto.setId(pupil.getId());
        responseDto.setSurname(pupil.getSurname());
        responseDto.setFirstName(pupil.getFirstName());
        responseDto.setAClass(pupil.getAClass());
        responseDto.setLogin(pupil.getLogin());
        responseDto.setDob(pupil.getDob());
        return responseDto;
    }

    private GradeResponseDto getGradeResponseDto(Grade grade) {
        GradeResponseDto gradeResponseDto = new GradeResponseDto();
        gradeResponseDto.setId(grade.getId());
        gradeResponseDto.setPupil(grade.getPupil());
        gradeResponseDto.setDiscipline(grade.getDiscipline());
        gradeResponseDto.setDate(grade.getDate());
        gradeResponseDto.setValue(grade.getValue());
        return gradeResponseDto;
    }
}
