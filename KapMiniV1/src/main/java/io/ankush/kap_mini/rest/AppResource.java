package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.AppDTO;
import io.ankush.kap_mini.service.AppService;
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
@RequestMapping(value = "/api/apps", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppResource {

    private final AppService appService;

    public AppResource(final AppService appService) {
        this.appService = appService;
    }

    @GetMapping()
    public ResponseEntity<List<AppDTO>> getAllApps() {
        return ResponseEntity.ok(appService.findAll());
    }

    @GetMapping("/{appId}")
    public ResponseEntity<AppDTO> getApp(@PathVariable(name = "appId") final UUID appId) {
        return ResponseEntity.ok(appService.get(appId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createApp(@RequestBody @Valid final AppDTO appDTO) {
        final UUID createdAppId = appService.create(appDTO);
        return new ResponseEntity<>(createdAppId, HttpStatus.CREATED);
    }

    @PutMapping("/{appId}")
    public ResponseEntity<UUID> updateApp(@PathVariable(name = "appId") final UUID appId,
            @RequestBody @Valid final AppDTO appDTO) {
        appService.update(appId, appDTO);
        return ResponseEntity.ok(appId);
    }

    @DeleteMapping("/{appId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteApp(@PathVariable(name = "appId") final UUID appId) {
        final ReferencedWarning referencedWarning = appService.getReferencedWarning(appId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        appService.delete(appId);
        return ResponseEntity.noContent().build();
    }

}
