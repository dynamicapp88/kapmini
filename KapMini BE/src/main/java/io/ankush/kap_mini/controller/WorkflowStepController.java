package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.Workflow;
import io.ankush.kap_mini.model.ActionType;
import io.ankush.kap_mini.model.StepType;
import io.ankush.kap_mini.model.WorkflowStepDTO;
import io.ankush.kap_mini.repos.WorkflowRepository;
import io.ankush.kap_mini.service.WorkflowStepService;
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
@RequestMapping("/workflowSteps")
public class WorkflowStepController {

    private final WorkflowStepService workflowStepService;
    private final WorkflowRepository workflowRepository;

    public WorkflowStepController(final WorkflowStepService workflowStepService,
            final WorkflowRepository workflowRepository) {
        this.workflowStepService = workflowStepService;
        this.workflowRepository = workflowRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("stepTypeValues", StepType.values());
        model.addAttribute("actionTypeValues", ActionType.values());
        model.addAttribute("workflowIdValues", workflowRepository.findAll(Sort.by("workflowId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Workflow::getWorkflowId, Workflow::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("workflowSteps", workflowStepService.findAll());
        return "workflowStep/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("workflowStep") final WorkflowStepDTO workflowStepDTO) {
        return "workflowStep/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("workflowStep") @Valid final WorkflowStepDTO workflowStepDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "workflowStep/add";
        }
        workflowStepService.create(workflowStepDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("workflowStep.create.success"));
        return "redirect:/workflowSteps";
    }

    @GetMapping("/edit/{workflowStepId}")
    public String edit(@PathVariable(name = "workflowStepId") final UUID workflowStepId,
            final Model model) {
        model.addAttribute("workflowStep", workflowStepService.get(workflowStepId));
        return "workflowStep/edit";
    }

    @PostMapping("/edit/{workflowStepId}")
    public String edit(@PathVariable(name = "workflowStepId") final UUID workflowStepId,
            @ModelAttribute("workflowStep") @Valid final WorkflowStepDTO workflowStepDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "workflowStep/edit";
        }
        workflowStepService.update(workflowStepId, workflowStepDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("workflowStep.update.success"));
        return "redirect:/workflowSteps";
    }

    @PostMapping("/delete/{workflowStepId}")
    public String delete(@PathVariable(name = "workflowStepId") final UUID workflowStepId,
            final RedirectAttributes redirectAttributes) {
        workflowStepService.delete(workflowStepId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("workflowStep.delete.success"));
        return "redirect:/workflowSteps";
    }

}
