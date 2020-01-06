package com.coolightman.app.controller;

import com.coolightman.app.dto.request.ParentRequestDto;
import com.coolightman.app.dto.response.PupilResponseDto;
import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.repository.PupilRepository;
import com.coolightman.app.service.AClassService;
import com.coolightman.app.service.ParentService;
import com.coolightman.app.service.PupilService;
import org.dozer.Mapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.coolightman.app.controller.PupilController.getPupilResponseDto;

@Controller
@RequestMapping("/parents")
public class ParentController {

    private final Mapper mapper;
    private final ParentService parentService;
    private final AClassService classService;
    private final PupilService pupilService;

    public ParentController(final Mapper mapper,
                            final ParentService parentService,
                            final AClassService classService,
                            final PupilService pupilService) {
        this.mapper = mapper;
        this.parentService = parentService;
        this.classService = classService;
        this.pupilService = pupilService;
    }

    @GetMapping()
    public String parentPage(Model model) {
        Pupil pupil = getCurrentPupil();
        String userText = "Choose grade period of " + pupil.getSurname() + " " + pupil.getFirstName();
        model.addAttribute("userText", userText);
        return "pupilAndParentPage.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/showSignUp/{id}")
    public String showSignUp(@PathVariable("id") long pupilId, Model model) {
        model.addAttribute("parent", new Parent());
        model.addAttribute("pupilId", pupilId);
        return "signUpParent.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/signUpParent/{id}")
    public String signUpParent(@PathVariable("id") long pupilId,
                               @Valid @ModelAttribute("parent") ParentRequestDto parentRequestDto,
                               BindingResult result,
                               Model model) {
        Parent parent = getEntity(parentRequestDto, pupilId);
        if (result.hasErrors()) {
            model.addAttribute("parent", parent);
            return "signUpParent.html";
        }
        parentService.save(parent);
        createPupilsList(model, parent.getPupil().getAClass().getId());
        return "listPupilsByClass.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/deleteParent/{id}")
    public String deleteParent(@PathVariable("id") long id, Model model) {
        Parent parent = parentService.findByID(id);
        Long pupilClassId = parent.getPupil().getAClass().getId();
        parentService.deleteByID(id);
        createPupilsList(model, pupilClassId);
        return "listPupilsByClass.html";
    }

    private void createPupilsList(final Model model, final Long aClassId) {
        final String className = classService.findByID(aClassId).getName();
        final List<PupilResponseDto> responseDtos = pupilService.findByClassName(className)
                .stream()
                .map(this::setPupilEntity)
                .collect(Collectors.toList());
        model.addAttribute("pupils", responseDtos);
        model.addAttribute("className", className);
    }

    private Parent getEntity(ParentRequestDto requestDto, Long pupilId) {
        Parent parent = new Parent();
        parent.setLogin(requestDto.getLogin());
        parent.setPassword(requestDto.getPassword());
        parent.setPupil(pupilService.findByID(pupilId));
        return parent;
    }

    private PupilResponseDto setPupilEntity(Pupil pupil) {
        return getPupilResponseDto(pupil);
    }

    private Pupil getCurrentPupil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String login = user.getUsername();
        return parentService.findParentByLogin(login).getPupil();
    }
}
