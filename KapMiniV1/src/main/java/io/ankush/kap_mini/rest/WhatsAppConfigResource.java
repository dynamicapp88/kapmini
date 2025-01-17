package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.WhatsAppConfigDTO;
import io.ankush.kap_mini.service.WhatsAppConfigService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping(value = "/api/whatsAppConfigs", produces = MediaType.APPLICATION_JSON_VALUE)
public class WhatsAppConfigResource {

    private final WhatsAppConfigService whatsAppConfigService;

    public WhatsAppConfigResource(final WhatsAppConfigService whatsAppConfigService) {
        this.whatsAppConfigService = whatsAppConfigService;
    }

    @GetMapping
    public ResponseEntity<List<WhatsAppConfigDTO>> getAllWhatsAppConfigs() {
        return ResponseEntity.ok(whatsAppConfigService.findAll());
    }

    @GetMapping("/{configID}")
    public ResponseEntity<WhatsAppConfigDTO> getWhatsAppConfig(
            @PathVariable(name = "configID") final Long configID) {
        return ResponseEntity.ok(whatsAppConfigService.get(configID));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createWhatsAppConfig(
            @RequestBody @Valid final WhatsAppConfigDTO whatsAppConfigDTO) {
        final Long createdConfigID = whatsAppConfigService.create(whatsAppConfigDTO);
        return new ResponseEntity<>(createdConfigID, HttpStatus.CREATED);
    }

    @PutMapping("/{configID}")
    public ResponseEntity<Long> updateWhatsAppConfig(
            @PathVariable(name = "configID") final Long configID,
            @RequestBody @Valid final WhatsAppConfigDTO whatsAppConfigDTO) {
        whatsAppConfigService.update(configID, whatsAppConfigDTO);
        return ResponseEntity.ok(configID);
    }

    @DeleteMapping("/{configID}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteWhatsAppConfig(
            @PathVariable(name = "configID") final Long configID) {
        whatsAppConfigService.delete(configID);
        return ResponseEntity.noContent().build();
    }

}
