package io.ankush.kap_mini.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationConfigurationDTO {

    private UUID configID;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @JsonProperty("isAction")
    private Boolean isAction;

    @NotNull
    @Size(max = 255)
    private String description;

    private LocalDateTime deleteAt;

}
