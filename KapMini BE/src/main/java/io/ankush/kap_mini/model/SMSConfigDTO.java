package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SMSConfigDTO {

    private UUID configId;

    @NotNull
    @Size(max = 255)
    private String smsApiEndpoint;

    @NotNull
    @Size(max = 255)
    private String authKey;

    @NotNull
    @Size(max = 255)
    private String senderNumber;

    private LocalDateTime deleteAt;

    @NotNull
    private UUID notificationTypeID;

}
