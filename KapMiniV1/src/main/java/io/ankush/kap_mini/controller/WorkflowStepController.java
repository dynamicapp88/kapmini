package io.ankush.kap_mini.controller;

import io.ankush.kap_mini.domain.Field;
import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.domain.Workflow;
import io.ankush.kap_mini.domain.WorkflowStep;
import io.ankush.kap_mini.model.ActionType;
import io.ankush.kap_mini.model.StepType;
import io.ankush.kap_mini.model.WorkflowStepDTO;
import io.ankush.kap_mini.repos.FieldRepository;
import io.ankush.kap_mini.repos.FormRepository;
import io.ankush.kap_mini.repos.WorkflowRepository;
import io.ankush.kap_mini.repos.WorkflowStepRepository;
import io.ankush.kap_mini.service.WorkflowStepService;
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
@RequestMapping("/workflowSteps")
public class WorkflowStepController {

    private final WorkflowStepService workflowStepService;
    private final WorkflowRepository workflowRepository;
    private final FormRepository formRepository;
    private final FieldRepository fieldRepository;
    private final WorkflowStepRepository workflowStepRepository;

    public WorkflowStepController(final WorkflowStepService workflowStepService,
            final WorkflowRepository workflowRepository, final FormRepository formRepository,
            final FieldRepository fieldRepository,
            final WorkflowStepRepository workflowStepRepository) {
        this.workflowStepService = workflowStepService;
        this.workflowRepository = workflowRepository;
        this.formRepository = formRepository;
        this.fieldRepository = fieldRepository;
        this.workflowStepRepository = workflowStepRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("stepTypeValues", StepType.values());
        model.addAttribute("actionTypeValues", ActionType.values());
        model.addAttribute("workflowIdValues", workflowRepository.findAll(Sort.by("workflowId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Workflow::getWorkflowId, Workflow::getName)));
        model.addAttribute("formIdValues", formRepository.findAll(Sort.by("formId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Form::getFormId, Form::getName)));
        model.addAttribute("fieldIdValues", fieldRepository.findAll(Sort.by("fieldId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Field::getFieldId, Field::getFieldName)));
        model.addAttribute("nextWorkFlowStepIdValues", workflowStepRepository.findAll(Sort.by("stepID"))
                .stream()
                .collect(CustomCollectors.toSortedMap(WorkflowStep::getStepID, WorkflowStep::getStepID)));
        model.addAttribute("prevWorkFlowStepIdValues", workflowStepRepository.findAll(Sort.by("stepID"))
                .stream()
                .collect(CustomCollectors.toSortedMap(WorkflowStep::getStepID, WorkflowStep::getStepID)));
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

    @GetMapping("/edit/{stepID}")
    public String edit(@PathVariable(name = "stepID") final UUID stepID, final Model model) {
        model.addAttribute("workflowStep", workflowStepService.get(stepID));
        return "workflowStep/edit";
    }

    @PostMapping("/edit/{stepID}")
    public String edit(@PathVariable(name = "stepID") final UUID stepID,
            @ModelAttribute("workflowStep") @Valid final WorkflowStepDTO workflowStepDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "workflowStep/edit";
        }
        workflowStepService.update(stepID, workflowStepDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("workflowStep.update.success"));
        return "redirect:/workflowSteps";
    }

    @PostMapping("/delete/{stepID}")
    public String delete(@PathVariable(name = "stepID") final UUID stepID,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = workflowStepService.getReferencedWarning(stepID);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            workflowStepService.delete(stepID);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("workflowStep.delete.success"));
        }
        return "redirect:/workflowSteps";
    }

}
