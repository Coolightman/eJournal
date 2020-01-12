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
import org.jetbrains.annotations.NotNull;
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

    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping()
    public String pupilPage(Model model) {
        Pupil pupil = getCurrentPupil();
        String userText = "Choose grade period of " + pupil.getSurname() + " " + pupil.getFirstName();
        model.addAttribute("userText", userText);
        return "pupilAndParentPage.html";
    }

    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping(value = "/gradesForToday")
    public String gradesForToday(Model model) {
        LocalDate date = LocalDate.now();
        final List<Grade> grades = gradeService.findByPupilAndDate(getCurrentPupil(), date);
        final List<GradeResponseDto> responseDtos = getGradeResponseDtos(grades);
        model.addAttribute("grades", responseDtos);
        addPupilText(model);
        return "listGrades.html";
    }

    @PreAuthorize("hasRole('ROLE_PUPIL')")
    private void addPupilText(final Model model) {
        Pupil pupil = getCurrentPupil();
        String firstName = pupil.getFirstName();
        String surname = pupil.getSurname();
        String aClassName = pupil.getAClass().getName();
        String pupilText = "Grades of pupil " + aClassName + " class \n" + surname + " " + firstName;
        model.addAttribute("pupilText", pupilText);
    }

    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping(value = "/gradesForYesterday")
    public String gradesForYesterday(Model model) {
        LocalDate date = LocalDate.now().minusDays(1L);
        final List<Grade> grades = gradeService.findByPupilAndDate(getCurrentPupil(), date);
        final List<GradeResponseDto> responseDtos = getGradeResponseDtos(grades);
        model.addAttribute("grades", responseDtos);
        addPupilText(model);
        return "listGrades.html";
    }

    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping(value = "/gradesForWeek")
    public String gradesForWeek(Model model) {
        LocalDate end = LocalDate.now().plusDays(1L);
        LocalDate begin = end.minusDays(7L);
        final List<Grade> grades = gradeService.findByPupilAndDatePeriod(getCurrentPupil(), begin, end);
        final List<GradeResponseDto> responseDtos = getGradeResponseDtos(grades);
        model.addAttribute("grades", responseDtos);
        addPupilText(model);
        return "listGrades.html";
    }

    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping(value = "/gradesForMonth")
    public String gradesForMonth(Model model) {
        LocalDate end = LocalDate.now().plusDays(1L);
        LocalDate begin = end.minusDays(30L);
        final List<Grade> grades = gradeService.findByPupilAndDatePeriod(getCurrentPupil(), begin, end);
        final List<GradeResponseDto> responseDtos = getGradeResponseDtos(grades);
        model.addAttribute("grades", responseDtos);
        addPupilText(model);
        return "listGrades.html";
    }

    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @GetMapping(value = "/gradesAll")
    public String gradesAll(Model model) {
        final List<Grade> grades = gradeService.findByPupil(getCurrentPupil());
        final List<GradeResponseDto> responseDtos = getGradeResponseDtos(grades);
        model.addAttribute("grades", responseDtos);
        addPupilText(model);
        return "listGrades.html";
    }

    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @PostMapping(value = "/gradesForDay")
    public String gradesForDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
                               Model model) {
        final List<Grade> grades = gradeService.findByPupilAndDate(getCurrentPupil(), day);
        final List<GradeResponseDto> responseDtos = getGradeResponseDtos(grades);
        model.addAttribute("grades", responseDtos);
        addPupilText(model);
        return "listGrades.html";
    }

    @PreAuthorize("hasRole('ROLE_PUPIL')")
    @PostMapping(value = "/gradesForPeriod")
    public String gradesForPeriod(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate firstDay,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDay,
                                  Model model) {
        final List<Grade> grades = gradeService.findByPupilAndDatePeriod(getCurrentPupil(),
                firstDay.minusDays(1L), lastDay.plusDays(1L));
        final List<GradeResponseDto> responseDtos = getGradeResponseDtos(grades);
        model.addAttribute("grades", responseDtos);
        addPupilText(model);
        return "listGrades.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/choosePupilsClass")
    public String choosePupilsClass(Model model) {
        model.addAttribute("classes", aClassService.findAll());
        return "choosePupilsClass.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/pupilsListByClass/{id}")
    public String pupilsListByClass(@PathVariable("id") long id, Model model) {
        createPupilsList(model, id);
        return "listPupilsByClass.html";
    }

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/updatePupil/{id}")
    public String updatePupil(@PathVariable("id") long id,
                              @Valid @ModelAttribute("pupil") PupilRequestDto pupilRequestDto,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "updatePupil.html";
        }
        Pupil pupil = getEntity(pupilRequestDto);
        pupil.setId(id);
        pupilService.update(pupil);
        createPupilsList(model, pupil.getAClass().getId());
        return "listPupilsByClass.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/showSignUp")
    public String showSignUp(Model model) {
        model.addAttribute("pupil", new Pupil());
        model.addAttribute("classes", aClassService.findAll());
        return "signUpPupil.html";
    }

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/deletePupil/{id}")
    public String deletePupil(@PathVariable("id") long id, Model model) {
        Long classId = pupilService.findByID(id).getAClass().getId();
        pupilService.deleteByID(id);
        createPupilsList(model, classId);
        return "listPupilsByClass.html";
    }

    @NotNull
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
