package com.coolightman.app.controller;

import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Discipline;
import com.coolightman.app.model.Grade;
import com.coolightman.app.model.Pupil;
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

import static com.coolightman.app.controller.TeacherController.getPupilIdValueMap;

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
     * @param newValue     the new grade
     * @param date         the date
     * @param model        the model
     * @return the string
     */
    @PostMapping(value = "/edit", params = "action=update")
    public String updateGrade(@RequestParam final Long disciplineId,
                              @RequestParam final Long pupilId,
                              @RequestParam final Short newValue,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date,
                              final Model model) {

        final Pupil pupil = pupilService.findByID(pupilId);
        final Discipline discipline = disciplineService.findByID(disciplineId);
        final AClass aClass = pupil.getAClass();

        if (gradeService.existsByPupilDisciplineAndDate(pupil, discipline, date)) {
//            Update
            final Grade grade = gradeService.findByPupilDisciplineAndDate(pupil, discipline, date);
            grade.setValue(newValue);
            gradeService.update(grade);
        } else {
//            Create
            final Grade grade = new Grade();
            grade.setPupil(pupil);
            grade.setDiscipline(discipline);
            grade.setValue(newValue);
            grade.setDate(date);
            gradeService.save(grade);
        }

        return createModelForLesson(model, discipline, aClass, date);
    }

    /**
     * Delete grade.
     *
     * @param disciplineId the discipline id
     * @param pupilId      the pupil id
     * @param currValue    the curr value
     * @param date         the date
     * @param model        the model
     * @return the string
     */
    @PostMapping(value = "/edit", params = "action=delete")
    public String deleteGrade(@RequestParam final Long disciplineId,
                              @RequestParam final Long pupilId,
                              @RequestParam final Short currValue,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date,
                              final Model model) {

        final Pupil pupil = pupilService.findByID(pupilId);
        final Discipline discipline = disciplineService.findByID(disciplineId);
        final AClass aClass = pupil.getAClass();

//        check for empty use "delete" button
        if (currValue != null) {
            final Grade grade = gradeService.findByPupilDisciplineAndDate(pupil, discipline, date);
            gradeService.delete(grade);
        }
        return createModelForLesson(model, discipline, aClass, date);
    }

    private String createModelForLesson(final Model model, final Discipline discipline,
                                        final AClass aClass, final LocalDate date) {

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
}
