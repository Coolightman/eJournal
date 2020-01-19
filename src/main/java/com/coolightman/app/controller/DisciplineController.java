package com.coolightman.app.controller;

import com.coolightman.app.dto.request.DisciplineRequestDto;
import com.coolightman.app.dto.response.DisciplineResponseDto;
import com.coolightman.app.model.Discipline;
import com.coolightman.app.service.DisciplineService;
import org.dozer.Mapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Discipline controller.
 */
@Controller
@RequestMapping("/disciplines")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DisciplineController {

    private final Mapper mapper;
    private final DisciplineService disciplineService;

    /**
     * Instantiates a new Discipline controller.
     *
     * @param mapper            the mapper
     * @param disciplineService the discipline service
     */
    public DisciplineController(final Mapper mapper,
                                final DisciplineService disciplineService) {
        this.mapper = mapper;
        this.disciplineService = disciplineService;
    }

    /**
     * List disciplines page.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping
    public String listDisciplines(final Model model) {
        createDisciplineList(model);
        return "listDisciplines.html";
    }

    /**
     * Show update page.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/showUpdate/{id}")
    public String showUpdate(@PathVariable("id") final Long id, final Model model) {
        model.addAttribute("discipline", disciplineService.findByID(id));
        return "updateDiscipline.html";
    }

    /**
     * Update discipline.
     *
     * @param disciplineRequestDto the discipline request dto
     * @param result               the result
     * @param model                the model
     * @return the string
     */
    @PostMapping("/updateDiscipline")
    public String updateDiscipline(@Valid @ModelAttribute("discipline") final DisciplineRequestDto disciplineRequestDto,
                                   final BindingResult result,
                                   final Model model) {

        if (result.hasErrors()) {
            return "updateDiscipline.html";
        }
        return updateAndGetPage(model, getEntity(disciplineRequestDto));
    }

    private String updateAndGetPage(final Model model, final Discipline discipline) {
        try {
            disciplineService.update(discipline);
            createDisciplineList(model);
            return "listDisciplines.html";
        } catch (RuntimeException except) {
            model.addAttribute("exceptMsg", except.getMessage());
            return "updateDiscipline.html";
        }
    }

    /**
     * Show sign up page.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/showSignUp")
    public String showSignUp(final Model model) {
        model.addAttribute("discipline", new Discipline());
        return "signUpDiscipline.html";
    }

    /**
     * Sign up discipline.
     *
     * @param disciplineRequestDto the discipline request dto
     * @param result               the result
     * @param model                the model
     * @return the string
     */
    @PostMapping("/signUpDiscipline")
    public String signUpDiscipline(@Valid @ModelAttribute("discipline") final DisciplineRequestDto disciplineRequestDto,
                                   final BindingResult result,
                                   final Model model) {

        if (result.hasErrors()) {
            return "signUpDiscipline.html";
        }
        return saveAndGetPage(model, getEntity(disciplineRequestDto));
    }

    private String saveAndGetPage(final Model model, final Discipline discipline) {
        try {
            disciplineService.save(discipline);
            createDisciplineList(model);
            return "listDisciplines.html";
        } catch (RuntimeException except) {
            model.addAttribute("exceptMsg", except.getMessage());
            return "signUpDiscipline.html";
        }
    }

    /**
     * Delete discipline.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/deleteDiscipline/{id}")
    public String deleteDiscipline(@PathVariable("id") final Long id, final Model model) {
        disciplineService.deleteByID(id);
        createDisciplineList(model);
        return "listDisciplines.html";
    }

    private void createDisciplineList(final Model model) {
        final List<DisciplineResponseDto> responseDtos = disciplineService.findAll()
                .stream()
                .map(this::setEntity)
                .collect(Collectors.toList());
        model.addAttribute("disciplines", responseDtos);
    }

    private Discipline getEntity(final DisciplineRequestDto requestDto) {
        return mapper.map(requestDto, Discipline.class);
    }

    private DisciplineResponseDto setEntity(final Discipline discipline) {
        return mapper.map(discipline, DisciplineResponseDto.class);
    }
}
