package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.UserSettingDTO;
import io.ankush.kap_mini.service.UserSettingService;
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
@RequestMapping(value = "/api/userSettings", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserSettingResource {

    private final UserSettingService userSettingService;

    public UserSettingResource(final UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    @GetMapping
    public ResponseEntity<List<UserSettingDTO>> getAllUserSettings() {
        return ResponseEntity.ok(userSettingService.findAll());
    }

    @GetMapping("/{settingID}")
    public ResponseEntity<UserSettingDTO> getUserSetting(
            @PathVariable(name = "settingID") final UUID settingID) {
        return ResponseEntity.ok(userSettingService.get(settingID));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createUserSetting(
            @RequestBody @Valid final UserSettingDTO userSettingDTO) {
        final UUID createdSettingID = userSettingService.create(userSettingDTO);
        return new ResponseEntity<>(createdSettingID, HttpStatus.CREATED);
    }

    @PutMapping("/{settingID}")
    public ResponseEntity<UUID> updateUserSetting(
            @PathVariable(name = "settingID") final UUID settingID,
            @RequestBody @Valid final UserSettingDTO userSettingDTO) {
        userSettingService.update(settingID, userSettingDTO);
        return ResponseEntity.ok(settingID);
    }

    @DeleteMapping("/{settingID}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUserSetting(
            @PathVariable(name = "settingID") final UUID settingID) {
        userSettingService.delete(settingID);
        return ResponseEntity.noContent().build();
    }

}
