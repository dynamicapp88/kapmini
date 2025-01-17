package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.model.FormDTO;
import io.ankush.kap_mini.repos.AppRepository;
import io.ankush.kap_mini.service.FormService;
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
@RequestMapping("/forms")
public class FormController {

    private final FormService formService;
    private final AppRepository appRepository;

    public FormController(final FormService formService, final AppRepository appRepository) {
        this.formService = formService;
        this.appRepository = appRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("appIDValues", appRepository.findAll(Sort.by("appId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(App::getAppId, App::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("forms", formService.findAll());
        return "form/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("form") final FormDTO formDTO) {
        return "form/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("form") @Valid final FormDTO formDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "form/add";
        }
        formService.create(formDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("form.create.success"));
        return "redirect:/forms";
    }

    @GetMapping("/edit/{formId}")
    public String edit(@PathVariable(name = "formId") final UUID formId, final Model model) {
        model.addAttribute("form", formService.get(formId));
        return "form/edit";
    }

    @PostMapping("/edit/{formId}")
    public String edit(@PathVariable(name = "formId") final UUID formId,
            @ModelAttribute("form") @Valid final FormDTO formDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "form/edit";
        }
        formService.update(formId, formDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("form.update.success"));
        return "redirect:/forms";
    }

    @PostMapping("/delete/{formId}")
    public String delete(@PathVariable(name = "formId") final UUID formId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = formService.getReferencedWarning(formId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            formService.delete(formId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("form.delete.success"));
        }
        return "redirect:/forms";
    }

}
