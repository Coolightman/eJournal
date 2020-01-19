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

/**
 * The type Parent controller.
 */
@Controller
@RequestMapping("/parents")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PARENT')")
public class ParentController {

    private final ParentService parentService;
    private final AClassService aClassService;
    private final PupilService pupilService;

    /**
     * Instantiates a new Parent controller.
     *
     * @param parentService the parent service
     * @param aClassService the class service
     * @param pupilService  the pupil service
     */
    public ParentController(final ParentService parentService,
                            final AClassService aClassService,
                            final PupilService pupilService) {
        this.parentService = parentService;
        this.aClassService = aClassService;
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
    public String parentPage(final Model model) {
        createPageText(model);
        return "pupilAndParentPage.html";
    }

    private void createPageText(Model model) {
        final Pupil pupil = getCurrentPupil();
        final String userText = "Choose grade period of " + pupil.getSurname() + " " + pupil.getFirstName();
        model.addAttribute("userText", userText);
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
    public String showSignUp(@PathVariable("id") final Long pupilId, final Model model) {
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
    public String signUpParent(@Valid @ModelAttribute("parent") final ParentRequestDto parentRequestDto,
                               final BindingResult result,
                               final Model model) {

        if (result.hasErrors()) {
            model.addAttribute("pupilId", parentRequestDto.getPupil());
            return "signUpParent.html";
        }
        return saveAndGetPage(model, getEntity(parentRequestDto));
    }

    private String saveAndGetPage(final Model model, final Parent parent) {
        try {
            parentService.save(parent);
            createPupilsList(model, parent.getPupil().getAClass());
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
    public String deleteParent(@PathVariable("id") final Long id, final Model model) {
        final Pupil pupil = parentService.findByID(id).getPupil();
        parentService.deleteByID(id);
        model.addAttribute("pupil", pupil);
        model.addAttribute("classes", aClassService.findAll());
        return "updatePupil.html";
    }

    private void createPupilsList(final Model model, final AClass aClass) {
        final List<PupilResponseDto> responseDtos = pupilService.findByClass(aClass)
                .stream()
                .map(this::setEntity)
                .collect(Collectors.toList());
        model.addAttribute("pupils", responseDtos);
        model.addAttribute("className", aClass.getName());
    }

    private Parent getEntity(final ParentRequestDto requestDto) {
        final Parent parent = new Parent();
        parent.setId(requestDto.getId());
        parent.setLogin(requestDto.getLogin());
        parent.setPassword(requestDto.getPassword());
        parent.setPupil(pupilService.findByID(requestDto.getPupil()));
        return parent;
    }

    private PupilResponseDto setEntity(final Pupil pupil) {
        final PupilResponseDto responseDto = new PupilResponseDto();
        responseDto.setId(pupil.getId());
        responseDto.setSurname(pupil.getSurname());
        responseDto.setFirstName(pupil.getFirstName());
        responseDto.setAClass(pupil.getAClass());
        responseDto.setLogin(pupil.getLogin());
        responseDto.setDob(pupil.getDob());
        return responseDto;
    }

    private Pupil getCurrentPupil() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User user = (User) authentication.getPrincipal();
        final String login = user.getUsername();
        return parentService.findByLogin(login).getPupil();
    }
}
