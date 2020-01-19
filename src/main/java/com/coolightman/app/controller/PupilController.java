package com.coolightman.app.controller;

import com.coolightman.app.dto.request.PupilRequestDto;
import com.coolightman.app.dto.response.GradeResponseDto;
import com.coolightman.app.dto.response.PupilResponseDto;
import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Grade;
import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.service.AClassService;
import com.coolightman.app.service.GradeService;
import com.coolightman.app.service.ParentService;
import com.coolightman.app.service.PupilService;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
    private final ParentService parentService;
    private final AClassService aClassService;
    private final GradeService gradeService;

    /**
     * Instantiates a new Pupil controller.
     *
     * @param pupilService  the pupil service
     * @param parentService the parent service
     * @param aClassService the a class service
     * @param gradeService  the grade service
     */
    public PupilController(final PupilService pupilService,
                           final ParentService parentService,
                           final AClassService aClassService,
                           final GradeService gradeService) {
        this.pupilService = pupilService;
        this.parentService = parentService;
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
    public String pupilPage(final Model model) {
        createPageText(model);
        return "pupilAndParentPage.html";
    }

    /**
     * Grades for today page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL') or hasRole('ROLE_PARENT')")
    @GetMapping("/gradesForToday")
    public String gradesForToday(final Model model) {
        createModel(model, gradeService.findByPupilAndDate(getCurrentPupil(), LocalDate.now()));
        return "listGrades.html";
    }

    /**
     * Grades for yesterday page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL') or hasRole('ROLE_PARENT')")
    @GetMapping("/gradesForYesterday")
    public String gradesForYesterday(final Model model) {
        final LocalDate date = LocalDate.now().minusDays(1L);
        createModel(model, gradeService.findByPupilAndDate(getCurrentPupil(), date));
        return "listGrades.html";
    }

    /**
     * Grades for week page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL') or hasRole('ROLE_PARENT')")
    @GetMapping("/gradesForWeek")
    public String gradesForWeek(final Model model) {
        final LocalDate lastDay = LocalDate.now().plusDays(1L);
        final LocalDate firstDay = lastDay.minusDays(7L);
        createModel(model, gradeService.findByPupilAndDatePeriod(getCurrentPupil(), firstDay, lastDay));
        return "listGrades.html";
    }

    /**
     * Grades for month page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL') or hasRole('ROLE_PARENT')")
    @GetMapping("/gradesForMonth")
    public String gradesForMonth(final Model model) {
        final LocalDate lastDay = LocalDate.now().plusDays(1L);
        final LocalDate firstDay = lastDay.minusDays(30L);
        createModel(model, gradeService.findByPupilAndDatePeriod(getCurrentPupil(), firstDay, lastDay));
        return "listGrades.html";
    }

    /**
     * Grades all page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PUPIL') or hasRole('ROLE_PARENT')")
    @GetMapping("/gradesAll")
    public String gradesAll(final Model model) {
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
    @PreAuthorize("hasRole('ROLE_PUPIL') or hasRole('ROLE_PARENT')")
    @PostMapping("/gradesForDay")
    public String gradesForDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate day,
                               final Model model) {
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
    @PreAuthorize("hasRole('ROLE_PUPIL') or hasRole('ROLE_PARENT')")
    @PostMapping("/gradesForPeriod")
    public String gradesForPeriod(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate firstDay,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate lastDay,
                                  final Model model) {
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
    @GetMapping("/choosePupilsClass")
    public String choosePupilsClass(final Model model) {
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
    @GetMapping("/pupilsListByClass/{id}")
    public String pupilsListByClass(@PathVariable("id") final Long id, final Model model) {
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
    public String showUpdate(@PathVariable("id") final Long id, final Model model) {
        createModelForUpdate(model, id);
        return "updatePupil.html";
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
    public String updatePupil(@Valid @ModelAttribute("pupil") final PupilRequestDto pupilRequestDto,
                              final BindingResult result,
                              final Model model) {

        if (result.hasErrors()) {
            createModelForUpdate(model, getEntity(pupilRequestDto).getId());
            return "updatePupil.html";
        }
        return updateAndGetPage(model, getEntity(pupilRequestDto));
    }

    @NotNull
    private String updateAndGetPage(final Model model, final Pupil pupil) {
        try {
            pupilService.update(pupil);
            createPupilsList(model, pupil.getAClass().getId());
            return "listPupilsByClass.html";
        } catch (RuntimeException except) {
            model.addAttribute("exceptMsg", except.getMessage());
            createModelForUpdate(model, pupil.getId());
            return "updatePupil.html";
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
    public String signUpPupil(@Valid @ModelAttribute("pupil") final PupilRequestDto pupilRequestDto,
                              final BindingResult result,
                              final Model model) {

        if (result.hasErrors()) {
            model.addAttribute("classes", aClassService.findAll());
            return "signUpPupil.html";
        }
        return saveAndGetPage(model, getEntity(pupilRequestDto));
    }

    private String saveAndGetPage(final Model model, final Pupil pupil) {
        try {
            pupilService.save(pupil);
            createPupilsList(model, pupil.getAClass().getId());
            return "listPupilsByClass.html";
        } catch (RuntimeException except) {
            model.addAttribute("classes", aClassService.findAll());
            model.addAttribute("exceptMsg", except.getMessage());
            return "signUpPupil.html";
        }
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
    public String deletePupil(@PathVariable("id") final Long id, final Model model) {
        final Long classId = pupilService.findByID(id).getAClass().getId();
        pupilService.deleteByID(id);
        createPupilsList(model, classId);
        return "listPupilsByClass.html";
    }

    private void createModelForUpdate(final Model model, final Long id) {
        final Pupil pupil = pupilService.findByID(id);
        final Parent parent = getParent(pupil);
        model.addAttribute("pupil", pupil);
        model.addAttribute("parent", parent);
        model.addAttribute("classes", aClassService.findAll());
    }

    private Parent getParent(final Pupil pupil) {
        if (parentService.existByPupil(pupil)) {
            return parentService.findByPupil(pupil);
        } else {
            return null;
        }
    }

    private void createPageText(Model model) {
        final Pupil pupil = getCurrentPupil();
        String pageText = "Choose grade period of " + pupil.getSurname() + " " + pupil.getFirstName();
        model.addAttribute("pageText", pageText);
    }

    private void createModel(final Model model, final List<Grade> grades) {
        final List<GradeResponseDto> responseDtos = setGradeEntities(grades);
        model.addAttribute("grades", responseDtos);
        createPupilText(model);
    }

    private void createPupilText(final Model model) {
        final Pupil pupil = getCurrentPupil();
        final String pupilText = "Grades of pupil " + pupil.getAClass().getName() + " class \n"
                + pupil.getSurname() + " " + pupil.getFirstName();
        model.addAttribute("pupilText", pupilText);
    }

    private List<GradeResponseDto> setGradeEntities(final List<Grade> grades) {
        return grades.stream()
                .map(this::setGradeEntity)
                .collect(Collectors.toList());
    }

    private Pupil getCurrentPupil() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User user = (User) authentication.getPrincipal();

        final String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()).get(0);

        if (role.equals("ROLE_PUPIL")) {
            return pupilService.findByLogin(user.getUsername());
        } else if (role.equals("ROLE_PARENT")) {
            return parentService.findByLogin(user.getUsername()).getPupil();
        } else {
            throw new RuntimeException();
        }
    }

    private void createPupilsList(final Model model, final Long aClassId) {
        final AClass aClass = aClassService.findByID(aClassId);
        final List<PupilResponseDto> responseDtos = pupilService.findByClass(aClass)
                .stream()
                .map(this::setEntity)
                .collect(Collectors.toList());
        model.addAttribute("pupils", responseDtos);
        model.addAttribute("className", aClass.getName());
    }

    private Pupil getEntity(final PupilRequestDto requestDto) {
        final Pupil pupil = new Pupil();
        pupil.setId(requestDto.getId());
        pupil.setLogin(requestDto.getLogin());
        pupil.setPassword(requestDto.getPassword());
        pupil.setFirstName(requestDto.getFirstName());
        pupil.setSurname(requestDto.getSurname());
        pupil.setAClass(aClassService.findByID(requestDto.getAClass()));
        pupil.setDob(requestDto.getDob());
        return pupil;
    }

    private PupilResponseDto setEntity(final Pupil pupil) {
        final PupilResponseDto responseDto = new PupilResponseDto();
        responseDto.setId(pupil.getId());
        responseDto.setSurname(pupil.getSurname());
        responseDto.setFirstName(pupil.getFirstName());
        responseDto.setAClass(pupil.getAClass());
        responseDto.setLogin(pupil.getLogin());
        responseDto.setDob(pupil.getDob());
        return responseDto;
    }

    private GradeResponseDto setGradeEntity(final Grade grade) {
        final GradeResponseDto gradeResponseDto = new GradeResponseDto();
        gradeResponseDto.setId(grade.getId());
        gradeResponseDto.setPupil(grade.getPupil());
        gradeResponseDto.setDiscipline(grade.getDiscipline());
        gradeResponseDto.setDate(grade.getDate());
        gradeResponseDto.setValue(grade.getValue());
        return gradeResponseDto;
    }
}
