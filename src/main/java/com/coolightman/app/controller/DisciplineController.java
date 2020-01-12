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

@Controller
@RequestMapping("/disciplines")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DisciplineController {

    private final Mapper mapper;
    private final DisciplineService disciplineService;

    public DisciplineController(final Mapper mapper,
                                final DisciplineService disciplineService) {
        this.mapper = mapper;
        this.disciplineService = disciplineService;
    }

    @GetMapping
    public String listDisciplines(Model model) {
        createDisciplineList(model);
        return "listDisciplines.html";
    }

    @GetMapping("/showUpdate/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("discipline", disciplineService.findByID(id));
        return "updateDiscipline.html";
    }

    @PostMapping("/updateDiscipline/{id}")
    public String updateDiscipline(@PathVariable("id") long id,
                                   @Valid @ModelAttribute("discipline") DisciplineRequestDto disciplineRequestDto,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            return "updateDiscipline.html";
        }
        Discipline discipline = getEntity(disciplineRequestDto);
        discipline.setId(id);
        disciplineService.update(discipline);
        createDisciplineList(model);
        return "listDisciplines.html";
    }

    @GetMapping("/showSignUp")
    public String showSignUp(Model model) {
        model.addAttribute("discipline", new Discipline());
        return "signUpDiscipline.html";
    }

    @PostMapping("/signUpDiscipline")
    public String signUpDiscipline(@Valid @ModelAttribute("discipline") DisciplineRequestDto disciplineRequestDto,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            return "signUpDiscipline.html";
        }
        Discipline discipline = getEntity(disciplineRequestDto);
        disciplineService.save(discipline);
        createDisciplineList(model);
        return "listDisciplines.html";
    }

    @GetMapping("/deleteDiscipline/{id}")
    public String deleteDiscipline(@PathVariable("id") long id, Model model) {
        disciplineService.deleteByID(id);
        createDisciplineList(model);
        return "listDisciplines.html";
    }

    private void createDisciplineList(Model model) {
        final List<DisciplineResponseDto> disciplineResponseDtos = disciplineService.findAll()
                .stream()
                .map(this::setEntity)
                .collect(Collectors.toList());
        model.addAttribute("disciplines", disciplineResponseDtos);
    }

    private Discipline getEntity(DisciplineRequestDto requestDto) {
        return mapper.map(requestDto, Discipline.class);
    }

    private DisciplineResponseDto setEntity(Discipline discipline) {
        return mapper.map(discipline, DisciplineResponseDto.class);
    }
}
