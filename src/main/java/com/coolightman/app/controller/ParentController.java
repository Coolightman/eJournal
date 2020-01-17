package com.coolightman.app.controller;

import com.coolightman.app.dto.request.ParentRequestDto;
import com.coolightman.app.dto.response.PupilResponseDto;
import com.coolightman.app.model.AClass;
import com.coolightman.app.model.Parent;
import com.coolightman.app.model.Pupil;
import com.coolightman.app.service.AClassService;
import com.coolightman.app.service.ParentService;
import com.coolightman.app.service.PupilService;
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

/**
 * The type Parent controller.
 */
@Controller
@RequestMapping("/parents")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PARENT')")
public class ParentController {

    private final ParentService parentService;
    private final AClassService classService;
    private final PupilService pupilService;

    /**
     * Instantiates a new Parent controller.
     *
     * @param parentService the parent service
     * @param classService  the class service
     * @param pupilService  the pupil service
     */
    public ParentController(final ParentService parentService,
                            final AClassService classService,
                            final PupilService pupilService) {
        this.parentService = parentService;
        this.classService = classService;
        this.pupilService = pupilService;
    }

    /**
     * Parent page.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_PARENT')")
    @GetMapping()
    public String parentPage(Model model) {
        model.addAttribute("userText", createPageText());
        return "pupilAndParentPage.html";
    }

    private String createPageText() {
        Pupil pupil = getCurrentPupil();
        return "Choose grade period of " + pupil.getSurname() + " " + pupil.getFirstName();
    }

    /**
     * Show sign up page.
     *
     * @param pupilId the pupil id
     * @param model   the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/showSignUp/{id}")
    public String showSignUp(@PathVariable("id") long pupilId, Model model) {
        model.addAttribute("parent", new Parent());
        model.addAttribute("pupilId", pupilId);
        return "signUpParent.html";
    }

    /**
     * Sign up parent.
     *
     * @param parentRequestDto the parent request dto
     * @param result           the result
     * @param model            the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/signUpParent")
    public String signUpParent(@Valid @ModelAttribute("parent") ParentRequestDto parentRequestDto,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            model.addAttribute("pupilId", parentRequestDto.getPupil());
            return "signUpParent.html";
        }
        return saveAndGetPage(model, getEntity(parentRequestDto));
    }

    private String saveAndGetPage(final Model model, final Parent parent) {
        try {
            parentService.save(parent);
            createPupilsList(model, parent.getPupil().getAClass().getId());
            return "listPupilsByClass.html";
        } catch (RuntimeException except) {
            model.addAttribute("pupilId", parent.getPupil().getId());
            model.addAttribute("exceptMsg", except.getMessage());
            return "signUpParent.html";
        }
    }

    /**
     * Delete parent.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/deleteParent/{id}")
    public String deleteParent(@PathVariable("id") long id, Model model) {
        Long pupilClassId = parentService.findByID(id).getPupil().getAClass().getId();
        parentService.deleteByID(id);
        createPupilsList(model, pupilClassId);
        return "listPupilsByClass.html";
    }

    private void createPupilsList(final Model model, final Long aClassId) {
        final AClass aClass = classService.findByID(aClassId);
        final List<PupilResponseDto> responseDtos = pupilService.findByClass(aClass)
                .stream()
                .map(this::setPupilEntity)
                .collect(Collectors.toList());
        model.addAttribute("pupils", responseDtos);
        model.addAttribute("className", aClass.getName());
    }

    private Parent getEntity(ParentRequestDto requestDto) {
        Parent parent = new Parent();
        parent.setId(requestDto.getId());
        parent.setLogin(requestDto.getLogin());
        parent.setPassword(requestDto.getPassword());
        parent.setPupil(pupilService.findByID(requestDto.getPupil()));
        return parent;
    }

    private PupilResponseDto setPupilEntity(Pupil pupil) {
        return getPupilResponseDto(pupil);
    }

    private Pupil getCurrentPupil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String login = user.getUsername();
        return parentService.findByLogin(login).getPupil();
    }
}
