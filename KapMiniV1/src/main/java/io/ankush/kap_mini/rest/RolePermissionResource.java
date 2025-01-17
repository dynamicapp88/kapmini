package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.RolePermissionDTO;
import io.ankush.kap_mini.service.RolePermissionService;
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
@RequestMapping(value = "/api/rolePermissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class RolePermissionResource {

    private final RolePermissionService rolePermissionService;

    public RolePermissionResource(final RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping
    public ResponseEntity<List<RolePermissionDTO>> getAllRolePermissions() {
        return ResponseEntity.ok(rolePermissionService.findAll());
    }

    @GetMapping("/{rolePermissionId}")
    public ResponseEntity<RolePermissionDTO> getRolePermission(
            @PathVariable(name = "rolePermissionId") final UUID rolePermissionId) {
        return ResponseEntity.ok(rolePermissionService.get(rolePermissionId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createRolePermission(
            @RequestBody @Valid final RolePermissionDTO rolePermissionDTO) {
        final UUID createdRolePermissionId = rolePermissionService.create(rolePermissionDTO);
        return new ResponseEntity<>(createdRolePermissionId, HttpStatus.CREATED);
    }

    @PutMapping("/{rolePermissionId}")
    public ResponseEntity<UUID> updateRolePermission(
            @PathVariable(name = "rolePermissionId") final UUID rolePermissionId,
            @RequestBody @Valid final RolePermissionDTO rolePermissionDTO) {
        rolePermissionService.update(rolePermissionId, rolePermissionDTO);
        return ResponseEntity.ok(rolePermissionId);
    }

    @DeleteMapping("/{rolePermissionId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRolePermission(
            @PathVariable(name = "rolePermissionId") final UUID rolePermissionId) {
        rolePermissionService.delete(rolePermissionId);
        return ResponseEntity.noContent().build();
    }

}
