package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.model.FieldDTO;
import io.ankush.kap_mini.model.FieldType;
import io.ankush.kap_mini.repos.FormRepository;
import io.ankush.kap_mini.service.FieldService;
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
@RequestMapping("/fields")
public class FieldController {

    private final FieldService fieldService;
    private final FormRepository formRepository;

    public FieldController(final FieldService fieldService, final FormRepository formRepository) {
        this.fieldService = fieldService;
        this.formRepository = formRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("fieldTypeValues", FieldType.values());
        model.addAttribute("formIdValues", formRepository.findAll(Sort.by("formId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Form::getFormId, Form::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("fields", fieldService.findAll());
        return "field/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("field") final FieldDTO fieldDTO) {
        return "field/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("field") @Valid final FieldDTO fieldDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "field/add";
        }
        fieldService.create(fieldDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("field.create.success"));
        return "redirect:/fields";
    }

    @GetMapping("/edit/{fieldId}")
    public String edit(@PathVariable(name = "fieldId") final UUID fieldId, final Model model) {
        model.addAttribute("field", fieldService.get(fieldId));
        return "field/edit";
    }

    @PostMapping("/edit/{fieldId}")
    public String edit(@PathVariable(name = "fieldId") final UUID fieldId,
            @ModelAttribute("field") @Valid final FieldDTO fieldDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "field/edit";
        }
        fieldService.update(fieldId, fieldDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("field.update.success"));
        return "redirect:/fields";
    }

    @PostMapping("/delete/{fieldId}")
    public String delete(@PathVariable(name = "fieldId") final UUID fieldId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = fieldService.getReferencedWarning(fieldId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            fieldService.delete(fieldId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("field.delete.success"));
        }
        return "redirect:/fields";
    }

}
