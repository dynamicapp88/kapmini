package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.RoleDTO;
import io.ankush.kap_mini.service.RoleService;
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
@RequestMapping(value = "/api/roles", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleResource {

    private final RoleService roleService;

    public RoleResource(final RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable(name = "roleId") final UUID roleId) {
        return ResponseEntity.ok(roleService.get(roleId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createRole(@RequestBody @Valid final RoleDTO roleDTO) {
        final UUID createdRoleId = roleService.create(roleDTO);
        return new ResponseEntity<>(createdRoleId, HttpStatus.CREATED);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<UUID> updateRole(@PathVariable(name = "roleId") final UUID roleId,
            @RequestBody @Valid final RoleDTO roleDTO) {
        roleService.update(roleId, roleDTO);
        return ResponseEntity.ok(roleId);
    }

    @DeleteMapping("/{roleId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRole(@PathVariable(name = "roleId") final UUID roleId) {
        final ReferencedWarning referencedWarning = roleService.getReferencedWarning(roleId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        roleService.delete(roleId);
        return ResponseEntity.noContent().build();
    }

}
