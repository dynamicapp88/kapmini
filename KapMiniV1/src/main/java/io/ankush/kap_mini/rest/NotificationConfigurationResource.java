package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.NotificationConfigurationDTO;
import io.ankush.kap_mini.service.NotificationConfigurationService;
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
@RequestMapping(value = "/api/notificationConfigurations", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationConfigurationResource {

    private final NotificationConfigurationService notificationConfigurationService;

    public NotificationConfigurationResource(
            final NotificationConfigurationService notificationConfigurationService) {
        this.notificationConfigurationService = notificationConfigurationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationConfigurationDTO>> getAllNotificationConfigurations() {
        return ResponseEntity.ok(notificationConfigurationService.findAll());
    }

    @GetMapping("/{configID}")
    public ResponseEntity<NotificationConfigurationDTO> getNotificationConfiguration(
            @PathVariable(name = "configID") final UUID configID) {
        return ResponseEntity.ok(notificationConfigurationService.get(configID));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createNotificationConfiguration(
            @RequestBody @Valid final NotificationConfigurationDTO notificationConfigurationDTO) {
        final UUID createdConfigID = notificationConfigurationService.create(notificationConfigurationDTO);
        return new ResponseEntity<>(createdConfigID, HttpStatus.CREATED);
    }

    @PutMapping("/{configID}")
    public ResponseEntity<UUID> updateNotificationConfiguration(
            @PathVariable(name = "configID") final UUID configID,
            @RequestBody @Valid final NotificationConfigurationDTO notificationConfigurationDTO) {
        notificationConfigurationService.update(configID, notificationConfigurationDTO);
        return ResponseEntity.ok(configID);
    }

    @DeleteMapping("/{configID}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteNotificationConfiguration(
            @PathVariable(name = "configID") final UUID configID) {
        final ReferencedWarning referencedWarning = notificationConfigurationService.getReferencedWarning(configID);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        notificationConfigurationService.delete(configID);
        return ResponseEntity.noContent().build();
    }

}
