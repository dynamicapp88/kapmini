package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.FieldDTO;
import io.ankush.kap_mini.service.FieldService;
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
@RequestMapping(value = "/api/fields", produces = MediaType.APPLICATION_JSON_VALUE)
public class FieldResource {

    private final FieldService fieldService;

    public FieldResource(final FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @GetMapping
    public ResponseEntity<List<FieldDTO>> getAllFields() {
        return ResponseEntity.ok(fieldService.findAll());
    }

    @GetMapping("/{fieldId}")
    public ResponseEntity<FieldDTO> getField(@PathVariable(name = "fieldId") final UUID fieldId) {
        return ResponseEntity.ok(fieldService.get(fieldId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createField(@RequestBody @Valid final FieldDTO fieldDTO) {
        final UUID createdFieldId = fieldService.create(fieldDTO);
        return new ResponseEntity<>(createdFieldId, HttpStatus.CREATED);
    }

    @PutMapping("/{fieldId}")
    public ResponseEntity<UUID> updateField(@PathVariable(name = "fieldId") final UUID fieldId,
            @RequestBody @Valid final FieldDTO fieldDTO) {
        fieldService.update(fieldId, fieldDTO);
        return ResponseEntity.ok(fieldId);
    }

    @DeleteMapping("/{fieldId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteField(@PathVariable(name = "fieldId") final UUID fieldId) {
        final ReferencedWarning referencedWarning = fieldService.getReferencedWarning(fieldId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        fieldService.delete(fieldId);
        return ResponseEntity.noContent().build();
    }

}
