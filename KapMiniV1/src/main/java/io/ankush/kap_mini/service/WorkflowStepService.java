package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.Field;
import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.domain.Workflow;
import io.ankush.kap_mini.domain.WorkflowStep;
import io.ankush.kap_mini.model.WorkflowStepDTO;
import io.ankush.kap_mini.repos.FieldRepository;
import io.ankush.kap_mini.repos.FormRepository;
import io.ankush.kap_mini.repos.WorkflowRepository;
import io.ankush.kap_mini.repos.WorkflowStepRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class WorkflowStepService {

    private final WorkflowStepRepository workflowStepRepository;
    private final WorkflowRepository workflowRepository;
    private final FormRepository formRepository;
    private final FieldRepository fieldRepository;

    public WorkflowStepService(final WorkflowStepRepository workflowStepRepository,
            final WorkflowRepository workflowRepository, final FormRepository formRepository,
            final FieldRepository fieldRepository) {
        this.workflowStepRepository = workflowStepRepository;
        this.workflowRepository = workflowRepository;
        this.formRepository = formRepository;
        this.fieldRepository = fieldRepository;
    }

    public List<WorkflowStepDTO> findAll() {
        final List<WorkflowStep> workflowSteps = workflowStepRepository.findAll(Sort.by("stepID"));
        return workflowSteps.stream()
                .map(workflowStep -> mapToDTO(workflowStep, new WorkflowStepDTO()))
                .toList();
    }

    public WorkflowStepDTO get(final UUID stepID) {
        return workflowStepRepository.findById(stepID)
                .map(workflowStep -> mapToDTO(workflowStep, new WorkflowStepDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final WorkflowStepDTO workflowStepDTO) {
        final WorkflowStep workflowStep = new WorkflowStep();
        mapToEntity(workflowStepDTO, workflowStep);
        return workflowStepRepository.save(workflowStep).getStepID();
    }

    public void update(final UUID stepID, final WorkflowStepDTO workflowStepDTO) {
        final WorkflowStep workflowStep = workflowStepRepository.findById(stepID)
                .orElseThrow(NotFoundException::new);
        mapToEntity(workflowStepDTO, workflowStep);
        workflowStepRepository.save(workflowStep);
    }

    public void delete(final UUID stepID) {
        workflowStepRepository.deleteById(stepID);
    }

    private WorkflowStepDTO mapToDTO(final WorkflowStep workflowStep,
            final WorkflowStepDTO workflowStepDTO) {
        workflowStepDTO.setStepID(workflowStep.getStepID());
        workflowStepDTO.setStepType(workflowStep.getStepType());
        workflowStepDTO.setCondition(workflowStep.getCondition());
        workflowStepDTO.setActionType(workflowStep.getActionType());
        workflowStepDTO.setActionDetails(workflowStep.getActionDetails());
        workflowStepDTO.setDeleteAt(workflowStep.getDeleteAt());
        workflowStepDTO.setSequenceOrder(workflowStep.getSequenceOrder());
        workflowStepDTO.setWorkflowId(workflowStep.getWorkflowId() == null ? null : workflowStep.getWorkflowId().getWorkflowId());
        workflowStepDTO.setFormId(workflowStep.getFormId() == null ? null : workflowStep.getFormId().getFormId());
        workflowStepDTO.setFieldId(workflowStep.getFieldId() == null ? null : workflowStep.getFieldId().getFieldId());
        workflowStepDTO.setNextWorkFlowStepId(workflowStep.getNextWorkFlowStepId() == null ? null : workflowStep.getNextWorkFlowStepId().getStepID());
        workflowStepDTO.setPrevWorkFlowStepId(workflowStep.getPrevWorkFlowStepId() == null ? null : workflowStep.getPrevWorkFlowStepId().getStepID());
        return workflowStepDTO;
    }

    private WorkflowStep mapToEntity(final WorkflowStepDTO workflowStepDTO,
            final WorkflowStep workflowStep) {
        workflowStep.setStepType(workflowStepDTO.getStepType());
        workflowStep.setCondition(workflowStepDTO.getCondition());
        workflowStep.setActionType(workflowStepDTO.getActionType());
        workflowStep.setActionDetails(workflowStepDTO.getActionDetails());
        workflowStep.setDeleteAt(workflowStepDTO.getDeleteAt());
        workflowStep.setSequenceOrder(workflowStepDTO.getSequenceOrder());
        final Workflow workflowId = workflowStepDTO.getWorkflowId() == null ? null : workflowRepository.findById(workflowStepDTO.getWorkflowId())
                .orElseThrow(() -> new NotFoundException("workflowId not found"));
        workflowStep.setWorkflowId(workflowId);
        final Form formId = workflowStepDTO.getFormId() == null ? null : formRepository.findById(workflowStepDTO.getFormId())
                .orElseThrow(() -> new NotFoundException("formId not found"));
        workflowStep.setFormId(formId);
        final Field fieldId = workflowStepDTO.getFieldId() == null ? null : fieldRepository.findById(workflowStepDTO.getFieldId())
                .orElseThrow(() -> new NotFoundException("fieldId not found"));
        workflowStep.setFieldId(fieldId);
        final WorkflowStep nextWorkFlowStepId = workflowStepDTO.getNextWorkFlowStepId() == null ? null : workflowStepRepository.findById(workflowStepDTO.getNextWorkFlowStepId())
                .orElseThrow(() -> new NotFoundException("nextWorkFlowStepId not found"));
        workflowStep.setNextWorkFlowStepId(nextWorkFlowStepId);
        final WorkflowStep prevWorkFlowStepId = workflowStepDTO.getPrevWorkFlowStepId() == null ? null : workflowStepRepository.findById(workflowStepDTO.getPrevWorkFlowStepId())
                .orElseThrow(() -> new NotFoundException("prevWorkFlowStepId not found"));
        workflowStep.setPrevWorkFlowStepId(prevWorkFlowStepId);
        return workflowStep;
    }

    public boolean nextWorkFlowStepIdExists(final UUID stepID) {
        return workflowStepRepository.existsByNextWorkFlowStepIdStepID(stepID);
    }

    public boolean prevWorkFlowStepIdExists(final UUID stepID) {
        return workflowStepRepository.existsByPrevWorkFlowStepIdStepID(stepID);
    }

    public ReferencedWarning getReferencedWarning(final UUID stepID) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final WorkflowStep workflowStep = workflowStepRepository.findById(stepID)
                .orElseThrow(NotFoundException::new);
        final WorkflowStep nextWorkFlowStepIdWorkflowStep = workflowStepRepository.findFirstByNextWorkFlowStepIdAndStepIDNot(workflowStep, workflowStep.getStepID());
        if (nextWorkFlowStepIdWorkflowStep != null) {
            referencedWarning.setKey("workflowStep.workflowStep.nextWorkFlowStepId.referenced");
            referencedWarning.addParam(nextWorkFlowStepIdWorkflowStep.getStepID());
            return referencedWarning;
        }
        final WorkflowStep prevWorkFlowStepIdWorkflowStep = workflowStepRepository.findFirstByPrevWorkFlowStepIdAndStepIDNot(workflowStep, workflowStep.getStepID());
        if (prevWorkFlowStepIdWorkflowStep != null) {
            referencedWarning.setKey("workflowStep.workflowStep.prevWorkFlowStepId.referenced");
            referencedWarning.addParam(prevWorkFlowStepIdWorkflowStep.getStepID());
            return referencedWarning;
        }
        return null;
    }

}
