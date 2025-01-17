package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.Workflow;
import io.ankush.kap_mini.domain.WorkflowStep;
import io.ankush.kap_mini.model.WorkflowDTO;
import io.ankush.kap_mini.repos.AppRepository;
import io.ankush.kap_mini.repos.WorkflowRepository;
import io.ankush.kap_mini.repos.WorkflowStepRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final AppRepository appRepository;
    private final WorkflowStepRepository workflowStepRepository;

    public WorkflowService(final WorkflowRepository workflowRepository,
            final AppRepository appRepository,
            final WorkflowStepRepository workflowStepRepository) {
        this.workflowRepository = workflowRepository;
        this.appRepository = appRepository;
        this.workflowStepRepository = workflowStepRepository;
    }

    public List<WorkflowDTO> findAll() {
        final List<Workflow> workflows = workflowRepository.findAll(Sort.by("workflowId"));
        return workflows.stream()
                .map(workflow -> mapToDTO(workflow, new WorkflowDTO()))
                .toList();
    }

    public WorkflowDTO get(final UUID workflowId) {
        return workflowRepository.findById(workflowId)
                .map(workflow -> mapToDTO(workflow, new WorkflowDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final WorkflowDTO workflowDTO) {
        final Workflow workflow = new Workflow();
        mapToEntity(workflowDTO, workflow);
        return workflowRepository.save(workflow).getWorkflowId();
    }

    public void update(final UUID workflowId, final WorkflowDTO workflowDTO) {
        final Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(workflowDTO, workflow);
        workflowRepository.save(workflow);
    }

    public void delete(final UUID workflowId) {
        workflowRepository.deleteById(workflowId);
    }

    private WorkflowDTO mapToDTO(final Workflow workflow, final WorkflowDTO workflowDTO) {
        workflowDTO.setWorkflowId(workflow.getWorkflowId());
        workflowDTO.setName(workflow.getName());
        workflowDTO.setDescription(workflow.getDescription());
        workflowDTO.setDeleteAt(workflow.getDeleteAt());
        workflowDTO.setAppId(workflow.getAppId() == null ? null : workflow.getAppId().getAppId());
        return workflowDTO;
    }

    private Workflow mapToEntity(final WorkflowDTO workflowDTO, final Workflow workflow) {
        workflow.setName(workflowDTO.getName());
        workflow.setDescription(workflowDTO.getDescription());
        workflow.setDeleteAt(workflowDTO.getDeleteAt());
        final App appId = workflowDTO.getAppId() == null ? null : appRepository.findById(workflowDTO.getAppId())
                .orElseThrow(() -> new NotFoundException("appId not found"));
        workflow.setAppId(appId);
        return workflow;
    }

    public ReferencedWarning getReferencedWarning(final UUID workflowId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(NotFoundException::new);
        final WorkflowStep workflowIdWorkflowStep = workflowStepRepository.findFirstByWorkflowId(workflow);
        if (workflowIdWorkflowStep != null) {
            referencedWarning.setKey("workflow.workflowStep.workflowId.referenced");
            referencedWarning.addParam(workflowIdWorkflowStep.getWorkflowStepId());
            return referencedWarning;
        }
        return null;
    }

}
