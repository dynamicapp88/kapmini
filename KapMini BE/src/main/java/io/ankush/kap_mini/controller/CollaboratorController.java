package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.SubDomain;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.CollaboratorDTO;
import io.ankush.kap_mini.repos.SubDomainRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.service.CollaboratorService;
import io.ankush.kap_mini.util.CustomCollectors;
import io.ankush.kap_mini.util.WebUtils;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/collaborators")
public class CollaboratorController {

    private final CollaboratorService collaboratorService;
    private final UserRepository userRepository;
    private final SubDomainRepository subDomainRepository;

    public CollaboratorController(final CollaboratorService collaboratorService,
            final UserRepository userRepository, final SubDomainRepository subDomainRepository) {
        this.collaboratorService = collaboratorService;
        this.userRepository = userRepository;
        this.subDomainRepository = subDomainRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userIDValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getName)));
        model.addAttribute("subdomainIdValues", subDomainRepository.findAll(Sort.by("subdomain"))
                .stream()
                .collect(CustomCollectors.toSortedMap(SubDomain::getSubdomain, SubDomain::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("collaborators", collaboratorService.findAll());
        return "collaborator/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("collaborator") final CollaboratorDTO collaboratorDTO) {
        return "collaborator/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("collaborator") @Valid final CollaboratorDTO collaboratorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "collaborator/add";
        }
        collaboratorService.create(collaboratorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("collaborator.create.success"));
        return "redirect:/collaborators";
    }

    @GetMapping("/edit/{collaboratorID}")
    public String edit(@PathVariable(name = "collaboratorID") final UUID collaboratorID,
            final Model model) {
        model.addAttribute("collaborator", collaboratorService.get(collaboratorID));
        return "collaborator/edit";
    }

    @PostMapping("/edit/{collaboratorID}")
    public String edit(@PathVariable(name = "collaboratorID") final UUID collaboratorID,
            @ModelAttribute("collaborator") @Valid final CollaboratorDTO collaboratorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "collaborator/edit";
        }
        collaboratorService.update(collaboratorID, collaboratorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("collaborator.update.success"));
        return "redirect:/collaborators";
    }

    @PostMapping("/delete/{collaboratorID}")
    public String delete(@PathVariable(name = "collaboratorID") final UUID collaboratorID,
            final RedirectAttributes redirectAttributes) {
        collaboratorService.delete(collaboratorID);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("collaborator.delete.success"));
        return "redirect:/collaborators";
    }

}
