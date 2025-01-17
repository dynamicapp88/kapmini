package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.WorkflowDTO;
import io.ankush.kap_mini.service.WorkflowService;
import io.ankush.kap_mini.util.ReferencedException;
import io.ankush.kap_mini.util.ReferencedWarning;
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
@RequestMapping(value = "/api/workflows", produces = MediaType.APPLICATION_JSON_VALUE)
public class WorkflowResource {

    private final WorkflowService workflowService;

    public WorkflowResource(final WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping
    public ResponseEntity<List<WorkflowDTO>> getAllWorkflows() {
        return ResponseEntity.ok(workflowService.findAll());
    }

    @GetMapping("/{workflowId}")
    public ResponseEntity<WorkflowDTO> getWorkflow(
            @PathVariable(name = "workflowId") final UUID workflowId) {
        return ResponseEntity.ok(workflowService.get(workflowId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createWorkflow(@RequestBody @Valid final WorkflowDTO workflowDTO) {
        final UUID createdWorkflowId = workflowService.create(workflowDTO);
        return new ResponseEntity<>(createdWorkflowId, HttpStatus.CREATED);
    }

    @PutMapping("/{workflowId}")
    public ResponseEntity<UUID> updateWorkflow(
            @PathVariable(name = "workflowId") final UUID workflowId,
            @RequestBody @Valid final WorkflowDTO workflowDTO) {
        workflowService.update(workflowId, workflowDTO);
        return ResponseEntity.ok(workflowId);
    }

    @DeleteMapping("/{workflowId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteWorkflow(
            @PathVariable(name = "workflowId") final UUID workflowId) {
        final ReferencedWarning referencedWarning = workflowService.getReferencedWarning(workflowId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        workflowService.delete(workflowId);
        return ResponseEntity.noContent().build();
    }

}
