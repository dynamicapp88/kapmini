package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.ResourceDTO;
import io.ankush.kap_mini.service.ResourceService;
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
@RequestMapping(value = "/api/resources", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResourceResource {

    private final ResourceService resourceService;

    public ResourceResource(final ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public ResponseEntity<List<ResourceDTO>> getAllResources() {
        return ResponseEntity.ok(resourceService.findAll());
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<ResourceDTO> getResource(
            @PathVariable(name = "resourceId") final UUID resourceId) {
        return ResponseEntity.ok(resourceService.get(resourceId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createResource(@RequestBody @Valid final ResourceDTO resourceDTO) {
        final UUID createdResourceId = resourceService.create(resourceDTO);
        return new ResponseEntity<>(createdResourceId, HttpStatus.CREATED);
    }

    @PutMapping("/{resourceId}")
    public ResponseEntity<UUID> updateResource(
            @PathVariable(name = "resourceId") final UUID resourceId,
            @RequestBody @Valid final ResourceDTO resourceDTO) {
        resourceService.update(resourceId, resourceDTO);
        return ResponseEntity.ok(resourceId);
    }

    @DeleteMapping("/{resourceId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteResource(
            @PathVariable(name = "resourceId") final UUID resourceId) {
        resourceService.delete(resourceId);
        return ResponseEntity.noContent().build();
    }

}
