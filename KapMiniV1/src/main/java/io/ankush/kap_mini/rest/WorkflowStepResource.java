package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.WorkflowStepDTO;
import io.ankush.kap_mini.service.WorkflowStepService;
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

    @GetMapping("/{stepID}")
    public ResponseEntity<WorkflowStepDTO> getWorkflowStep(
            @PathVariable(name = "stepID") final UUID stepID) {
        return ResponseEntity.ok(workflowStepService.get(stepID));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createWorkflowStep(
            @RequestBody @Valid final WorkflowStepDTO workflowStepDTO) {
        final UUID createdStepID = workflowStepService.create(workflowStepDTO);
        return new ResponseEntity<>(createdStepID, HttpStatus.CREATED);
    }

    @PutMapping("/{stepID}")
    public ResponseEntity<UUID> updateWorkflowStep(@PathVariable(name = "stepID") final UUID stepID,
            @RequestBody @Valid final WorkflowStepDTO workflowStepDTO) {
        workflowStepService.update(stepID, workflowStepDTO);
        return ResponseEntity.ok(stepID);
    }

    @DeleteMapping("/{stepID}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteWorkflowStep(
            @PathVariable(name = "stepID") final UUID stepID) {
        final ReferencedWarning referencedWarning = workflowStepService.getReferencedWarning(stepID);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        workflowStepService.delete(stepID);
        return ResponseEntity.noContent().build();
    }

}
