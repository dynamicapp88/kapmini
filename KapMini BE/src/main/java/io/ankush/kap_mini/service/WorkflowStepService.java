package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.Workflow;
import io.ankush.kap_mini.domain.WorkflowStep;
import io.ankush.kap_mini.model.WorkflowStepDTO;
import io.ankush.kap_mini.repos.WorkflowRepository;
import io.ankush.kap_mini.repos.WorkflowStepRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class WorkflowStepService {

    private final WorkflowStepRepository workflowStepRepository;
    private final WorkflowRepository workflowRepository;

    public WorkflowStepService(final WorkflowStepRepository workflowStepRepository,
            final WorkflowRepository workflowRepository) {
        this.workflowStepRepository = workflowStepRepository;
        this.workflowRepository = workflowRepository;
    }

    public List<WorkflowStepDTO> findAll() {
        final List<WorkflowStep> workflowSteps = workflowStepRepository.findAll(Sort.by("workflowStepId"));
        return workflowSteps.stream()
                .map(workflowStep -> mapToDTO(workflowStep, new WorkflowStepDTO()))
                .toList();
    }

    public WorkflowStepDTO get(final UUID workflowStepId) {
        return workflowStepRepository.findById(workflowStepId)
                .map(workflowStep -> mapToDTO(workflowStep, new WorkflowStepDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final WorkflowStepDTO workflowStepDTO) {
        final WorkflowStep workflowStep = new WorkflowStep();
        mapToEntity(workflowStepDTO, workflowStep);
        return workflowStepRepository.save(workflowStep).getWorkflowStepId();
    }

    public void update(final UUID workflowStepId, final WorkflowStepDTO workflowStepDTO) {
        final WorkflowStep workflowStep = workflowStepRepository.findById(workflowStepId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(workflowStepDTO, workflowStep);
        workflowStepRepository.save(workflowStep);
    }

    public void delete(final UUID workflowStepId) {
        workflowStepRepository.deleteById(workflowStepId);
    }

    private WorkflowStepDTO mapToDTO(final WorkflowStep workflowStep,
            final WorkflowStepDTO workflowStepDTO) {
        workflowStepDTO.setWorkflowStepId(workflowStep.getWorkflowStepId());
        workflowStepDTO.setStepType(workflowStep.getStepType());
        workflowStepDTO.setCondition(workflowStep.getCondition());
        workflowStepDTO.setActionType(workflowStep.getActionType());
        workflowStepDTO.setActionDetails(workflowStep.getActionDetails());
        workflowStepDTO.setDeleteAt(workflowStep.getDeleteAt());
        workflowStepDTO.setWorkflowId(workflowStep.getWorkflowId() == null ? null : workflowStep.getWorkflowId().getWorkflowId());
        return workflowStepDTO;
    }

    private WorkflowStep mapToEntity(final WorkflowStepDTO workflowStepDTO,
            final WorkflowStep workflowStep) {
        workflowStep.setStepType(workflowStepDTO.getStepType());
        workflowStep.setCondition(workflowStepDTO.getCondition());
        workflowStep.setActionType(workflowStepDTO.getActionType());
        workflowStep.setActionDetails(workflowStepDTO.getActionDetails());
        workflowStep.setDeleteAt(workflowStepDTO.getDeleteAt());
        final Workflow workflowId = workflowStepDTO.getWorkflowId() == null ? null : workflowRepository.findById(workflowStepDTO.getWorkflowId())
                .orElseThrow(() -> new NotFoundException("workflowId not found"));
        workflowStep.setWorkflowId(workflowId);
        return workflowStep;
    }

}
