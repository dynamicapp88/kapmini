package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.WorkflowStepDTO;
import io.ankush.kap_mini.service.WorkflowStepService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/workflowSteps", produces = MediaType.APPLICATION_JSON_VALUE)
public class WorkflowStepResource {

    private final WorkflowStepService workflowStepService;

    public WorkflowStepResource(final WorkflowStepService workflowStepService) {
        this.workflowStepService = workflowStepService;
    }

    @GetMapping
    public ResponseEntity<List<WorkflowStepDTO>> getAllWorkflowSteps() {
        return ResponseEntity.ok(workflowStepService.findAll());
    }

    @GetMapping("/{workflowStepId}")
    public ResponseEntity<WorkflowStepDTO> getWorkflowStep(
            @PathVariable(name = "workflowStepId") final UUID workflowStepId) {
        return ResponseEntity.ok(workflowStepService.get(workflowStepId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createWorkflowStep(
            @RequestBody @Valid final WorkflowStepDTO workflowStepDTO) {
        final UUID createdWorkflowStepId = workflowStepService.create(workflowStepDTO);
        return new ResponseEntity<>(createdWorkflowStepId, HttpStatus.CREATED);
    }

    @PutMapping("/{workflowStepId}")
    public ResponseEntity<UUID> updateWorkflowStep(
            @PathVariable(name = "workflowStepId") final UUID workflowStepId,
            @RequestBody @Valid final WorkflowStepDTO workflowStepDTO) {
        workflowStepService.update(workflowStepId, workflowStepDTO);
        return ResponseEntity.ok(workflowStepId);
    }

    @DeleteMapping("/{workflowStepId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteWorkflowStep(
            @PathVariable(name = "workflowStepId") final UUID workflowStepId) {
        workflowStepService.delete(workflowStepId);
        return ResponseEntity.noContent().build();
    }

}
