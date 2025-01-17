package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InAppConfigDTO {

    private UUID configID;

    @NotNull
    private Integer displayDuration;

    @NotNull
    @Size(max = 255)
    private String priority;

    private LocalDateTime deleteAt;

    @NotNull
    private UUID notificationTypeID;

}
