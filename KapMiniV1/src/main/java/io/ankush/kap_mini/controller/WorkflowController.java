package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.model.WorkflowDTO;
import io.ankush.kap_mini.repos.AppRepository;
import io.ankush.kap_mini.service.WorkflowService;
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
@RequestMapping("/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;
    private final AppRepository appRepository;

    public WorkflowController(final WorkflowService workflowService,
            final AppRepository appRepository) {
        this.workflowService = workflowService;
        this.appRepository = appRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("appIdValues", appRepository.findAll(Sort.by("appId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(App::getAppId, App::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("workflows", workflowService.findAll());
        return "workflow/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("workflow") final WorkflowDTO workflowDTO) {
        return "workflow/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("workflow") @Valid final WorkflowDTO workflowDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "workflow/add";
        }
        workflowService.create(workflowDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("workflow.create.success"));
        return "redirect:/workflows";
    }

    @GetMapping("/edit/{workflowId}")
    public String edit(@PathVariable(name = "workflowId") final UUID workflowId,
            final Model model) {
        model.addAttribute("workflow", workflowService.get(workflowId));
        return "workflow/edit";
    }

    @PostMapping("/edit/{workflowId}")
    public String edit(@PathVariable(name = "workflowId") final UUID workflowId,
            @ModelAttribute("workflow") @Valid final WorkflowDTO workflowDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "workflow/edit";
        }
        workflowService.update(workflowId, workflowDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("workflow.update.success"));
        return "redirect:/workflows";
    }

    @PostMapping("/delete/{workflowId}")
    public String delete(@PathVariable(name = "workflowId") final UUID workflowId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = workflowService.getReferencedWarning(workflowId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            workflowService.delete(workflowId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("workflow.delete.success"));
        }
        return "redirect:/workflows";
    }

}
