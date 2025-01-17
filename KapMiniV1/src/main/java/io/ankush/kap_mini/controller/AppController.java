package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.SubDomain;
import io.ankush.kap_mini.model.AppDTO;
import io.ankush.kap_mini.model.Isactive;
import io.ankush.kap_mini.model.Isproduction;
import io.ankush.kap_mini.repos.SubDomainRepository;
import io.ankush.kap_mini.service.AppService;
import io.ankush.kap_mini.util.CustomCollectors;
import io.ankush.kap_mini.util.ReferencedWarning;
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
@RequestMapping("/apps")
public class AppController {

    private final AppService appService;
    private final SubDomainRepository subDomainRepository;

    public AppController(final AppService appService,
            final SubDomainRepository subDomainRepository) {
        this.appService = appService;
        this.subDomainRepository = subDomainRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("isActiveValues", Isactive.values());
        model.addAttribute("isProductionValues", Isproduction.values());
        model.addAttribute("subdomainIdValues", subDomainRepository.findAll(Sort.by("subdomain"))
                .stream()
                .collect(CustomCollectors.toSortedMap(SubDomain::getSubdomain, SubDomain::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("apps", appService.findAll());
        return "app/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("app") final AppDTO appDTO) {
        return "app/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("app") @Valid final AppDTO appDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "app/add";
        }
        appService.create(appDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("app.create.success"));
        return "redirect:/apps";
    }

    @GetMapping("/edit/{appId}")
    public String edit(@PathVariable(name = "appId") final UUID appId, final Model model) {
        model.addAttribute("app", appService.get(appId));
        return "app/edit";
    }

    @PostMapping("/edit/{appId}")
    public String edit(@PathVariable(name = "appId") final UUID appId,
            @ModelAttribute("app") @Valid final AppDTO appDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "app/edit";
        }
        appService.update(appId, appDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("app.update.success"));
        return "redirect:/apps";
    }

    @PostMapping("/delete/{appId}")
    public String delete(@PathVariable(name = "appId") final UUID appId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = appService.getReferencedWarning(appId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            appService.delete(appId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("app.delete.success"));
        }
        return "redirect:/apps";
    }

}
