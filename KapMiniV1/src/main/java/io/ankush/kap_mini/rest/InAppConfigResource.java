package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.InAppConfigDTO;
import io.ankush.kap_mini.service.InAppConfigService;
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
@RequestMapping(value = "/api/inAppConfigs", produces = MediaType.APPLICATION_JSON_VALUE)
public class InAppConfigResource {

    private final InAppConfigService inAppConfigService;

    public InAppConfigResource(final InAppConfigService inAppConfigService) {
        this.inAppConfigService = inAppConfigService;
    }

    @GetMapping
    public ResponseEntity<List<InAppConfigDTO>> getAllInAppConfigs() {
        return ResponseEntity.ok(inAppConfigService.findAll());
    }

    @GetMapping("/{configID}")
    public ResponseEntity<InAppConfigDTO> getInAppConfig(
            @PathVariable(name = "configID") final UUID configID) {
        return ResponseEntity.ok(inAppConfigService.get(configID));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createInAppConfig(
            @RequestBody @Valid final InAppConfigDTO inAppConfigDTO) {
        final UUID createdConfigID = inAppConfigService.create(inAppConfigDTO);
        return new ResponseEntity<>(createdConfigID, HttpStatus.CREATED);
    }

    @PutMapping("/{configID}")
    public ResponseEntity<UUID> updateInAppConfig(
            @PathVariable(name = "configID") final UUID configID,
            @RequestBody @Valid final InAppConfigDTO inAppConfigDTO) {
        inAppConfigService.update(configID, inAppConfigDTO);
        return ResponseEntity.ok(configID);
    }

    @DeleteMapping("/{configID}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteInAppConfig(
            @PathVariable(name = "configID") final UUID configID) {
        inAppConfigService.delete(configID);
        return ResponseEntity.noContent().build();
    }

}
