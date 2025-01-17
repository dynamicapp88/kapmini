package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.PermissionDTO;
import io.ankush.kap_mini.service.PermissionService;
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
@RequestMapping(value = "/api/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissionResource {

    private final PermissionService permissionService;

    public PermissionResource(final PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @GetMapping("/{premissionId}")
    public ResponseEntity<PermissionDTO> getPermission(
            @PathVariable(name = "premissionId") final UUID premissionId) {
        return ResponseEntity.ok(permissionService.get(premissionId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createPermission(
            @RequestBody @Valid final PermissionDTO permissionDTO) {
        final UUID createdPremissionId = permissionService.create(permissionDTO);
        return new ResponseEntity<>(createdPremissionId, HttpStatus.CREATED);
    }

    @PutMapping("/{premissionId}")
    public ResponseEntity<UUID> updatePermission(
            @PathVariable(name = "premissionId") final UUID premissionId,
            @RequestBody @Valid final PermissionDTO permissionDTO) {
        permissionService.update(premissionId, permissionDTO);
        return ResponseEntity.ok(premissionId);
    }

    @DeleteMapping("/{premissionId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePermission(
            @PathVariable(name = "premissionId") final UUID premissionId) {
        final ReferencedWarning referencedWarning = permissionService.getReferencedWarning(premissionId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        permissionService.delete(premissionId);
        return ResponseEntity.noContent().build();
    }

}
