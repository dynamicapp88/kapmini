package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WhatsAppConfigDTO {

    private Long configID;

    @Size(max = 255)
    private String whatsappApiEndpoint;

    @Size(max = 255)
    private String authKey;

    @Size(max = 255)
    private String businessNumber;

    private LocalDateTime deleteAt;

    @NotNull
    private UUID notificationTypeID;

}
