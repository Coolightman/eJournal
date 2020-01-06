package com.coolightman.app.controller;

import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Discipline;
import com.coolightman.app.model.Grade;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.service.DisciplineService;
import com.coolightman.app.service.GradeService;
import com.coolightman.app.service.PupilService;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/grades")
@PreAuthorize("hasRole('ROLE_TEACHER')")
public class GradeController {

    private final PupilService pupilService;
    private final DisciplineService disciplineService;
    private final GradeService gradeService;

    public GradeController(final PupilService pupilService,
                           final DisciplineService disciplineService,
                           final GradeService gradeService) {
        this.pupilService = pupilService;
        this.disciplineService = disciplineService;
        this.gradeService = gradeService;
    }

    @PostMapping(value = "/edit", params = "action=update")
    public String updateGrade(@RequestParam Long disciplineId,
                              @RequestParam Long pupilId,
                              @RequestParam Short newGrade,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              Model model) {
        Pupil pupil = pupilService.findByID(pupilId);
        Discipline discipline = disciplineService.findByID(disciplineId);
        AClass aClass = pupil.getAClass();
        final List<Grade> gradeForLesson = gradeService.findByPupilDisciplineAndDate(pupil, discipline, date);
        if (gradeForLesson.size() != 0) {
            Grade grade = gradeForLesson.get(0);
            grade.setValue(newGrade);
            gradeService.update(grade);
        } else {
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

    @PostMapping(value = "/edit", params = "action=delete")
    public String deleteGrade(@RequestParam Long disciplineId,
                              @RequestParam Long pupilId,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              Model model) {
        Pupil pupil = pupilService.findByID(pupilId);
        Discipline discipline = disciplineService.findByID(disciplineId);
        AClass aClass = pupil.getAClass();
        final List<Grade> gradeList = gradeService.findByPupilDisciplineAndDate(pupil, discipline, date);
        if (gradeList.size() != 0) {
            Grade grade = gradeList.get(0);
            gradeService.delete(grade);
        }

        createModelForLesson(model, discipline, aClass, date);
        return "lessonPupilsList.html";
    }

    private void createModelForLesson(final Model model,
                                      final Discipline discipline,
                                      final AClass aClass,
                                      final LocalDate date) {
        final List<Grade> gradeList = gradeService.findByClassDisciplineAndDate(aClass, discipline, date);
        final List<Pupil> pupilList = pupilService.findByClassName(aClass.getName());

//        map (currentPupil.id, grade for current lesson)
        Map<Long, String> gradeMap = pupilList.stream()
                .collect(Collectors.toMap(com.coolightman.app.model.User::getId,
                        pupil1 -> getGrade(pupil1, gradeList)));

        String lessonMsg = getLessonMsg(discipline, aClass, date);
        model.addAttribute("pupils", pupilList);
        model.addAttribute("discipline", discipline);
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
}
