package com.coolightman.app.controller;

import com.coolightman.app.model.*;
import com.coolightman.app.service.DisciplineService;
import com.coolightman.app.service.GradeService;
import com.coolightman.app.service.PupilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Grade controller.
 */
@Controller
@RequestMapping("/grades")
@PreAuthorize("hasRole('ROLE_TEACHER')")
@Slf4j
public class GradeController {

    private final PupilService pupilService;
    private final DisciplineService disciplineService;
    private final GradeService gradeService;

    /**
     * Instantiates a new Grade controller.
     *
     * @param pupilService      the pupil service
     * @param disciplineService the discipline service
     * @param gradeService      the grade service
     */
    public GradeController(final PupilService pupilService,
                           final DisciplineService disciplineService,
                           final GradeService gradeService) {
        this.pupilService = pupilService;
        this.disciplineService = disciplineService;
        this.gradeService = gradeService;
    }

    /**
     * Create/Update grade.
     *
     * @param disciplineId the discipline id
     * @param pupilId      the pupil id
     * @param newGrade     the new grade
     * @param date         the date
     * @param model        the model
     * @return the string
     */
    @PostMapping(value = "/edit", params = "action=update")
    public String updateGrade(@RequestParam Long disciplineId,
                              @RequestParam Long pupilId,
                              @RequestParam Short newGrade,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              Model model) {

        Pupil pupil = pupilService.findByID(pupilId);
        Discipline discipline = disciplineService.findByID(disciplineId);
        AClass aClass = pupil.getAClass();

        if (gradeService.existsByPupilAndDisciplineAndDate(pupil, discipline, date)) {
//            Update
            final Grade grade = gradeService.findByPupilDisciplineAndDate(pupil, discipline, date);
            grade.setValue(newGrade);
            gradeService.update(grade);
        } else {
//            Create
            Grade grade = new Grade();
            grade.setPupil(pupil);
            grade.setDiscipline(discipline);
            grade.setValue(newGrade);
            grade.setDate(date);
            gradeService.save(grade);
        }

        createModelForLesson(model, discipline, aClass, date);
        return "lessonPupilsList.html";
    }

    /**
     * Delete grade.
     *
     * @param disciplineId the discipline id
     * @param pupilId      the pupil id
     * @param date         the date
     * @param model        the model
     * @return the string
     */
    @PostMapping(value = "/edit", params = "action=delete")
    public String deleteGrade(@RequestParam Long disciplineId,
                              @RequestParam Long pupilId,
                              @RequestParam Short currGrade,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              Model model) {

        Pupil pupil = pupilService.findByID(pupilId);
        Discipline discipline = disciplineService.findByID(disciplineId);
        AClass aClass = pupil.getAClass();

        if (currGrade != null) {
            Grade grade = gradeService.findByPupilDisciplineAndDate(pupil, discipline, date);
            gradeService.delete(grade);
        }
        createModelForLesson(model, discipline, aClass, date);
        return "lessonPupilsList.html";
    }

    private void createModelForLesson(final Model model, final Discipline discipline,
                                      final AClass aClass, final LocalDate date) {

        final List<Pupil> pupilList = pupilService.findByClass(aClass);
        final List<Grade> grades = gradeService.findByClassDisciplineAndDate(aClass, discipline, date);

        Map<Long, String> gradeMap = pupilList.stream()
                .collect(Collectors.toMap(BaseClass::getId, pupil -> {

                    final Optional<Grade> any = grades.stream()
                            .filter(grade -> grade.getPupil().getId().equals(pupil.getId()))
                            .findAny();
                    return any.map(grade -> grade.getValue().toString()).orElse("");

                }));

        model.addAttribute("pupils", pupilList);
        model.addAttribute("discipline", discipline);
        model.addAttribute("grades", gradeMap);
        model.addAttribute("date", date);
        createModelMsg(model, discipline, aClass, date);
    }

    private void createModelMsg(final Model model, final Discipline discipline,
                                final AClass aClass, final LocalDate date) {

        String lessonMsg = discipline.getName() + " lesson in " + aClass.getName() + " class " + date;
        model.addAttribute("lessonMsg", lessonMsg);
    }
}
