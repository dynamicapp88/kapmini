package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.SMSConfigDTO;
import io.ankush.kap_mini.service.SMSConfigService;
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
@RequestMapping(value = "/api/sMSConfigs", produces = MediaType.APPLICATION_JSON_VALUE)
public class SMSConfigResource {

    private final SMSConfigService sMSConfigService;

    public SMSConfigResource(final SMSConfigService sMSConfigService) {
        this.sMSConfigService = sMSConfigService;
    }

    @GetMapping
    public ResponseEntity<List<SMSConfigDTO>> getAllSMSConfigs() {
        return ResponseEntity.ok(sMSConfigService.findAll());
    }

    @GetMapping("/{configId}")
    public ResponseEntity<SMSConfigDTO> getSMSConfig(
            @PathVariable(name = "configId") final UUID configId) {
        return ResponseEntity.ok(sMSConfigService.get(configId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createSMSConfig(
            @RequestBody @Valid final SMSConfigDTO sMSConfigDTO) {
        final UUID createdConfigId = sMSConfigService.create(sMSConfigDTO);
        return new ResponseEntity<>(createdConfigId, HttpStatus.CREATED);
    }

    @PutMapping("/{configId}")
    public ResponseEntity<UUID> updateSMSConfig(
            @PathVariable(name = "configId") final UUID configId,
            @RequestBody @Valid final SMSConfigDTO sMSConfigDTO) {
        sMSConfigService.update(configId, sMSConfigDTO);
        return ResponseEntity.ok(configId);
    }

    @DeleteMapping("/{configId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSMSConfig(
            @PathVariable(name = "configId") final UUID configId) {
        sMSConfigService.delete(configId);
        return ResponseEntity.noContent().build();
    }

}
