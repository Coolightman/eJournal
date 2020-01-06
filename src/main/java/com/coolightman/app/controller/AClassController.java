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

@Controller
@RequestMapping("/classes")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AClassController {

    private final Mapper mapper;
    private final AClassService classService;

    public AClassController(final Mapper mapper,
                            final AClassService classService) {
        this.mapper = mapper;
        this.classService = classService;
    }

    @GetMapping
    public String listAClasses(Model model) {
        createClassList(model);
        return "listAClasses.html";
    }

    @GetMapping("/showUpdate/{id}")
    public String showUpdate(@PathVariable("id") long id, Model model) {
        model.addAttribute("class", classService.findByID(id));
        return "updateAClass.html";
    }

    @PostMapping("/updateClass/{id}")
    public String updateClass(@PathVariable("id") long id,
                              @Valid @ModelAttribute("class") AClassRequestDto aClassRequestDto,
                              BindingResult result,
                              Model model) {
        AClass aClass = getEntity(aClassRequestDto);
        aClass.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("class", aClass);
            return "updateAClass.html";
        }
        classService.update(aClass);
        createClassList(model);
        return "listAClasses.html";
    }

    @GetMapping("/showSignUp")
    public String showSignUp(Model model) {
        model.addAttribute("class", new AClass());
        return "signUpAClass.html";
    }

    @PostMapping("/signUpClass")
    public String signUpClass(@Valid @ModelAttribute("aClass") AClassRequestDto aClassRequestDto,
                              BindingResult result,
                              Model model) {
        AClass aClass = getEntity(aClassRequestDto);
        if (result.hasErrors()) {
            model.addAttribute("class", aClass);
            return "signUpAClass.html";
        }
        classService.save(aClass);
        createClassList(model);
        return "listAClasses.html";
    }

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
