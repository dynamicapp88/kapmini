package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.UserRoleDTO;
import io.ankush.kap_mini.service.UserRoleService;
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
@RequestMapping(value = "/api/userRoles", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRoleResource {

    private final UserRoleService userRoleService;

    public UserRoleResource(final UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping
    public ResponseEntity<List<UserRoleDTO>> getAllUserRoles() {
        return ResponseEntity.ok(userRoleService.findAll());
    }

    @GetMapping("/{userRoleId}")
    public ResponseEntity<UserRoleDTO> getUserRole(
            @PathVariable(name = "userRoleId") final UUID userRoleId) {
        return ResponseEntity.ok(userRoleService.get(userRoleId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createUserRole(@RequestBody @Valid final UserRoleDTO userRoleDTO) {
        final UUID createdUserRoleId = userRoleService.create(userRoleDTO);
        return new ResponseEntity<>(createdUserRoleId, HttpStatus.CREATED);
    }

    @PutMapping("/{userRoleId}")
    public ResponseEntity<UUID> updateUserRole(
            @PathVariable(name = "userRoleId") final UUID userRoleId,
            @RequestBody @Valid final UserRoleDTO userRoleDTO) {
        userRoleService.update(userRoleId, userRoleDTO);
        return ResponseEntity.ok(userRoleId);
    }

    @DeleteMapping("/{userRoleId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUserRole(
            @PathVariable(name = "userRoleId") final UUID userRoleId) {
        userRoleService.delete(userRoleId);
        return ResponseEntity.noContent().build();
    }

}
