package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.NotificationConfigurationTypeDTO;
import io.ankush.kap_mini.service.NotificationConfigurationTypeService;
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
@RequestMapping(value = "/api/notificationConfigurationTypes", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationConfigurationTypeResource {

    private final NotificationConfigurationTypeService notificationConfigurationTypeService;

    public NotificationConfigurationTypeResource(
            final NotificationConfigurationTypeService notificationConfigurationTypeService) {
        this.notificationConfigurationTypeService = notificationConfigurationTypeService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationConfigurationTypeDTO>> getAllNotificationConfigurationTypes(
            ) {
        return ResponseEntity.ok(notificationConfigurationTypeService.findAll());
    }

    @GetMapping("/{configTypeID}")
    public ResponseEntity<NotificationConfigurationTypeDTO> getNotificationConfigurationType(
            @PathVariable(name = "configTypeID") final UUID configTypeID) {
        return ResponseEntity.ok(notificationConfigurationTypeService.get(configTypeID));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createNotificationConfigurationType(
            @RequestBody @Valid final NotificationConfigurationTypeDTO notificationConfigurationTypeDTO) {
        final UUID createdConfigTypeID = notificationConfigurationTypeService.create(notificationConfigurationTypeDTO);
        return new ResponseEntity<>(createdConfigTypeID, HttpStatus.CREATED);
    }

    @PutMapping("/{configTypeID}")
    public ResponseEntity<UUID> updateNotificationConfigurationType(
            @PathVariable(name = "configTypeID") final UUID configTypeID,
            @RequestBody @Valid final NotificationConfigurationTypeDTO notificationConfigurationTypeDTO) {
        notificationConfigurationTypeService.update(configTypeID, notificationConfigurationTypeDTO);
        return ResponseEntity.ok(configTypeID);
    }

    @DeleteMapping("/{configTypeID}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteNotificationConfigurationType(
            @PathVariable(name = "configTypeID") final UUID configTypeID) {
        final ReferencedWarning referencedWarning = notificationConfigurationTypeService.getReferencedWarning(configTypeID);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        notificationConfigurationTypeService.delete(configTypeID);
        return ResponseEntity.noContent().build();
    }

}
