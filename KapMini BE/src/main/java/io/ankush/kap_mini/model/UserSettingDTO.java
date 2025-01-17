package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserSettingDTO {

    private UUID settingID;

    @NotNull
    private Theme theme;

    @NotNull
    private Language language;

    @Size(max = 255)
    private String extraConfig;

    private LocalDateTime deleteAt;

    @NotNull
    private UUID userId;

}
