package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EmailConfigDTO {

    private UUID configId;

    @Size(max = 255)
    private String smtpServer;

    private Integer port;

    private Boolean authRequired;

    private LocalDateTime deleteAt;

    @NotNull
    private UUID notificationTypeID;

}
