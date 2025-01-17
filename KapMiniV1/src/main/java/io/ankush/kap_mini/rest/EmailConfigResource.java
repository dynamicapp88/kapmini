package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.EmailConfigDTO;
import io.ankush.kap_mini.service.EmailConfigService;
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
@RequestMapping(value = "/api/emailConfigs", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailConfigResource {

    private final EmailConfigService emailConfigService;

    public EmailConfigResource(final EmailConfigService emailConfigService) {
        this.emailConfigService = emailConfigService;
    }

    @GetMapping
    public ResponseEntity<List<EmailConfigDTO>> getAllEmailConfigs() {
        return ResponseEntity.ok(emailConfigService.findAll());
    }

    @GetMapping("/{configId}")
    public ResponseEntity<EmailConfigDTO> getEmailConfig(
            @PathVariable(name = "configId") final UUID configId) {
        return ResponseEntity.ok(emailConfigService.get(configId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createEmailConfig(
            @RequestBody @Valid final EmailConfigDTO emailConfigDTO) {
        final UUID createdConfigId = emailConfigService.create(emailConfigDTO);
        return new ResponseEntity<>(createdConfigId, HttpStatus.CREATED);
    }

    @PutMapping("/{configId}")
    public ResponseEntity<UUID> updateEmailConfig(
            @PathVariable(name = "configId") final UUID configId,
            @RequestBody @Valid final EmailConfigDTO emailConfigDTO) {
        emailConfigService.update(configId, emailConfigDTO);
        return ResponseEntity.ok(configId);
    }

    @DeleteMapping("/{configId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEmailConfig(
            @PathVariable(name = "configId") final UUID configId) {
        emailConfigService.delete(configId);
        return ResponseEntity.noContent().build();
    }

}
