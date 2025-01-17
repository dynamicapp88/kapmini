package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.RecordDTO;
import io.ankush.kap_mini.repos.FormRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.service.RecordService;
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
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;
    private final FormRepository formRepository;
    private final UserRepository userRepository;

    public RecordController(final RecordService recordService, final FormRepository formRepository,
            final UserRepository userRepository) {
        this.recordService = recordService;
        this.formRepository = formRepository;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("formIdValues", formRepository.findAll(Sort.by("formId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Form::getFormId, Form::getName)));
        model.addAttribute("createdByValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getName)));
        model.addAttribute("updatedByValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("records", recordService.findAll());
        return "record/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("record") final RecordDTO recordDTO) {
        return "record/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("record") @Valid final RecordDTO recordDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "record/add";
        }
        recordService.create(recordDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("record.create.success"));
        return "redirect:/records";
    }

    @GetMapping("/edit/{recordId}")
    public String edit(@PathVariable(name = "recordId") final UUID recordId, final Model model) {
        model.addAttribute("record", recordService.get(recordId));
        return "record/edit";
    }

    @PostMapping("/edit/{recordId}")
    public String edit(@PathVariable(name = "recordId") final UUID recordId,
            @ModelAttribute("record") @Valid final RecordDTO recordDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "record/edit";
        }
        recordService.update(recordId, recordDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("record.update.success"));
        return "redirect:/records";
    }

    @PostMapping("/delete/{recordId}")
    public String delete(@PathVariable(name = "recordId") final UUID recordId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = recordService.getReferencedWarning(recordId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            recordService.delete(recordId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("record.delete.success"));
        }
        return "redirect:/records";
    }

}
