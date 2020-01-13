package com.coolightman.app.controller;

import com.coolightman.app.dto.request.AClassRequestDto;
import com.coolightman.app.dto.response.AClassResponseDto;
import com.coolightman.app.model.AClass;
import com.coolightman.app.service.AClassService;
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
 * The type AClass controller.
 */
@Controller
@RequestMapping("/classes")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AClassController {

    private final Mapper mapper;
    private final AClassService classService;

    /**
     * Instantiates a new AClass controller.
     *
     * @param mapper       the mapper
     * @param classService the class service
     */
    public AClassController(final Mapper mapper,
                            final AClassService classService) {
        this.mapper = mapper;
        this.classService = classService;
    }

    /**
     * List a classes page.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping
    public String listAClasses(Model model) {
        createClassList(model);
        return "listAClasses.html";
    }

    /**
     * Show update page.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/showUpdate/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("class", classService.findByID(id));
        return "updateAClass.html";
    }

    /**
     * Update class.
     *
     * @param aClassRequestDto the a class request dto
     * @param result           the result
     * @param model            the model
     * @return the string
     */
    @PostMapping("/updateClass")
    public String updateClass(@Valid @ModelAttribute("class") AClassRequestDto aClassRequestDto,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            return "updateAClass.html";
        }
        AClass aClass = getEntity(aClassRequestDto);
        classService.update(aClass);
        createClassList(model);
        return "listAClasses.html";
    }

    /**
     * Show sign up page.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/showSignUp")
    public String showSignUp(Model model) {
        model.addAttribute("class", new AClass());
        return "signUpAClass.html";
    }

    /**
     * Sign up class.
     *
     * @param aClassRequestDto the a class request dto
     * @param result           the result
     * @param model            the model
     * @return the string
     */
    @PostMapping("/signUpClass")
    public String signUpClass(@Valid @ModelAttribute("class") AClassRequestDto aClassRequestDto,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            return "signUpAClass.html";
        }
        AClass aClass = getEntity(aClassRequestDto);
        classService.save(aClass);
        createClassList(model);
        return "listAClasses.html";
    }

    /**
     * Delete class.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/deleteClass/{id}")
    public String deleteClass(@PathVariable("id") long id, Model model) {
        classService.deleteByID(id);
        createClassList(model);
        return "listAClasses.html";
    }

    private void createClassList(Model model) {
        final List<AClassResponseDto> aClassResponseDtos = classService.findAll()
                .stream()
                .map(this::setEntity)
                .collect(Collectors.toList());
        model.addAttribute("classes", aClassResponseDtos);
    }

    private AClass getEntity(AClassRequestDto requestDto) {
        return mapper.map(requestDto, AClass.class);
    }

    private AClassResponseDto setEntity(AClass aClass) {
        return mapper.map(aClass, AClassResponseDto.class);
    }
}
