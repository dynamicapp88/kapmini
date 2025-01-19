package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.TemplateDTO;
import io.ankush.kap_mini.service.TemplateService;
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
@RequestMapping(value = "/api/templates", produces = MediaType.APPLICATION_JSON_VALUE)
public class TemplateResource {

    private final TemplateService templateService;

    public TemplateResource(final TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public ResponseEntity<List<TemplateDTO>> getAllTemplates() {
        return ResponseEntity.ok(templateService.findAll());
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<TemplateDTO> getTemplate(
            @PathVariable(name = "templateId") final UUID templateId) {
        return ResponseEntity.ok(templateService.get(templateId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createTemplate(@RequestBody @Valid final TemplateDTO templateDTO) {
        final UUID createdTemplateId = templateService.create(templateDTO);
        return new ResponseEntity<>(createdTemplateId, HttpStatus.CREATED);
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<UUID> updateTemplate(
            @PathVariable(name = "templateId") final UUID templateId,
            @RequestBody @Valid final TemplateDTO templateDTO) {
        templateService.update(templateId, templateDTO);
        return ResponseEntity.ok(templateId);
    }

    @DeleteMapping("/{templateId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTemplate(
            @PathVariable(name = "templateId") final UUID templateId) {
        templateService.delete(templateId);
        return ResponseEntity.noContent().build();
    }

}
