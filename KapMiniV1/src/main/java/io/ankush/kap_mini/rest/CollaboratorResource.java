package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.CollaboratorDTO;
import io.ankush.kap_mini.service.CollaboratorService;
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
@RequestMapping(value = "/api/collaborators", produces = MediaType.APPLICATION_JSON_VALUE)
public class CollaboratorResource {

    private final CollaboratorService collaboratorService;

    public CollaboratorResource(final CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    @GetMapping
    public ResponseEntity<List<CollaboratorDTO>> getAllCollaborators() {
        return ResponseEntity.ok(collaboratorService.findAll());
    }

    @GetMapping("/{collaboratorID}")
    public ResponseEntity<CollaboratorDTO> getCollaborator(
            @PathVariable(name = "collaboratorID") final UUID collaboratorID) {
        return ResponseEntity.ok(collaboratorService.get(collaboratorID));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createCollaborator(
            @RequestBody @Valid final CollaboratorDTO collaboratorDTO) {
        final UUID createdCollaboratorID = collaboratorService.create(collaboratorDTO);
        return new ResponseEntity<>(createdCollaboratorID, HttpStatus.CREATED);
    }

    @PutMapping("/{collaboratorID}")
    public ResponseEntity<UUID> updateCollaborator(
            @PathVariable(name = "collaboratorID") final UUID collaboratorID,
            @RequestBody @Valid final CollaboratorDTO collaboratorDTO) {
        collaboratorService.update(collaboratorID, collaboratorDTO);
        return ResponseEntity.ok(collaboratorID);
    }

    @DeleteMapping("/{collaboratorID}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCollaborator(
            @PathVariable(name = "collaboratorID") final UUID collaboratorID) {
        collaboratorService.delete(collaboratorID);
        return ResponseEntity.noContent().build();
    }

}
