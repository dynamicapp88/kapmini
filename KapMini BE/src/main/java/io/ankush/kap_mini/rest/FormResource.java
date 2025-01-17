package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.FormDTO;
import io.ankush.kap_mini.service.FormService;
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
@RequestMapping(value = "/api/forms", produces = MediaType.APPLICATION_JSON_VALUE)
public class FormResource {

    private final FormService formService;

    public FormResource(final FormService formService) {
        this.formService = formService;
    }

    @GetMapping
    public ResponseEntity<List<FormDTO>> getAllForms() {
        return ResponseEntity.ok(formService.findAll());
    }

    @GetMapping("/{formId}")
    public ResponseEntity<FormDTO> getForm(@PathVariable(name = "formId") final UUID formId) {
        return ResponseEntity.ok(formService.get(formId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createForm(@RequestBody @Valid final FormDTO formDTO) {
        final UUID createdFormId = formService.create(formDTO);
        return new ResponseEntity<>(createdFormId, HttpStatus.CREATED);
    }

    @PutMapping("/{formId}")
    public ResponseEntity<UUID> updateForm(@PathVariable(name = "formId") final UUID formId,
            @RequestBody @Valid final FormDTO formDTO) {
        formService.update(formId, formDTO);
        return ResponseEntity.ok(formId);
    }

    @DeleteMapping("/{formId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteForm(@PathVariable(name = "formId") final UUID formId) {
        final ReferencedWarning referencedWarning = formService.getReferencedWarning(formId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        formService.delete(formId);
        return ResponseEntity.noContent().build();
    }

}
