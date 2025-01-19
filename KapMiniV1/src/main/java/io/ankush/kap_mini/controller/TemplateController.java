package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.model.TemplateDTO;
import io.ankush.kap_mini.service.TemplateService;
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
@RequestMapping("/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(final TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("templates", templateService.findAll());
        return "template/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("template") final TemplateDTO templateDTO) {
        return "template/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("template") @Valid final TemplateDTO templateDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "template/add";
        }
        templateService.create(templateDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("template.create.success"));
        return "redirect:/templates";
    }

    @GetMapping("/edit/{templateId}")
    public String edit(@PathVariable(name = "templateId") final UUID templateId,
            final Model model) {
        model.addAttribute("template", templateService.get(templateId));
        return "template/edit";
    }

    @PostMapping("/edit/{templateId}")
    public String edit(@PathVariable(name = "templateId") final UUID templateId,
            @ModelAttribute("template") @Valid final TemplateDTO templateDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "template/edit";
        }
        templateService.update(templateId, templateDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("template.update.success"));
        return "redirect:/templates";
    }

    @PostMapping("/delete/{templateId}")
    public String delete(@PathVariable(name = "templateId") final UUID templateId,
            final RedirectAttributes redirectAttributes) {
        templateService.delete(templateId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("template.delete.success"));
        return "redirect:/templates";
    }

}
 