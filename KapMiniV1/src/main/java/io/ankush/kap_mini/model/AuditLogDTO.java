package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuditLogDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String entityName;

    @NotNull
    @Size(max = 255)
    private String entityID;

    @Size(max = 255)
    private String actionType;

    @Size(max = 255)
    private String actionDetails;

    @NotNull
    @Size(max = 255)
    private String performedBy;

    @NotNull
    @Size(max = 255)
    private String ip;

    @Size(max = 255)
    private String sessionID;

    @Size(max = 255)
    private String userAgent;

    @NotNull
    private LocalDateTime lastLoginAt;

    @NotNull
    private LocalDateTime lastActivityAt;

    @NotNull
    @Size(max = 255)
    private String actionOutcome;

    @NotNull
    private Double processingTime;

    @NotNull
    private UUID userId;

}
