package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.model.SubDomainDTO;
import io.ankush.kap_mini.service.SubDomainService;
import io.ankush.kap_mini.util.ReferencedWarning;
import io.ankush.kap_mini.util.WebUtils;
import jakarta.validation.Valid;
import java.util.UUID;
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
@RequestMapping("/subDomains")
public class SubDomainController {

    private final SubDomainService subDomainService;

    public SubDomainController(final SubDomainService subDomainService) {
        this.subDomainService = subDomainService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("subDomains", subDomainService.findAll());
        return "subDomain/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("subDomain") final SubDomainDTO subDomainDTO) {
        return "subDomain/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("subDomain") @Valid final SubDomainDTO subDomainDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "subDomain/add";
        }
        subDomainService.create(subDomainDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("subDomain.create.success"));
        return "redirect:/subDomains";
    }

    @GetMapping("/edit/{subdomain}")
    public String edit(@PathVariable(name = "subdomain") final UUID subdomain, final Model model) {
        model.addAttribute("subDomain", subDomainService.get(subdomain));
        return "subDomain/edit";
    }

    @PostMapping("/edit/{subdomain}")
    public String edit(@PathVariable(name = "subdomain") final UUID subdomain,
            @ModelAttribute("subDomain") @Valid final SubDomainDTO subDomainDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "subDomain/edit";
        }
        subDomainService.update(subdomain, subDomainDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("subDomain.update.success"));
        return "redirect:/subDomains";
    }

    @PostMapping("/delete/{subdomain}")
    public String delete(@PathVariable(name = "subdomain") final UUID subdomain,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = subDomainService.getReferencedWarning(subdomain);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            subDomainService.delete(subdomain);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("subDomain.delete.success"));
        }
        return "redirect:/subDomains";
    }

}
