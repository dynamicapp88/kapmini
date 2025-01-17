package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.model.ResourceDTO;
import io.ankush.kap_mini.service.ResourceService;
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
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(final ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("resources", resourceService.findAll());
        return "resource/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("resource") final ResourceDTO resourceDTO) {
        return "resource/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("resource") @Valid final ResourceDTO resourceDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "resource/add";
        }
        resourceService.create(resourceDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("resource.create.success"));
        return "redirect:/resources";
    }

    @GetMapping("/edit/{resourceId}")
    public String edit(@PathVariable(name = "resourceId") final UUID resourceId,
            final Model model) {
        model.addAttribute("resource", resourceService.get(resourceId));
        return "resource/edit";
    }

    @PostMapping("/edit/{resourceId}")
    public String edit(@PathVariable(name = "resourceId") final UUID resourceId,
            @ModelAttribute("resource") @Valid final ResourceDTO resourceDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "resource/edit";
        }
        resourceService.update(resourceId, resourceDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("resource.update.success"));
        return "redirect:/resources";
    }

    @PostMapping("/delete/{resourceId}")
    public String delete(@PathVariable(name = "resourceId") final UUID resourceId,
            final RedirectAttributes redirectAttributes) {
        resourceService.delete(resourceId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("resource.delete.success"));
        return "redirect:/resources";
    }

}
