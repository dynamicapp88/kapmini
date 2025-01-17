package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HistoryDTO {

    private UUID historyID;

    @NotNull
    @Size(max = 255)
    private String tableName;

    @Size(max = 255)
    private String actionType;

    @Size(max = 255)
    private String actionDetails;

    @NotNull
    @Size(max = 255)
    private String performedBy;

    private LocalDateTime deleteAt;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID appID;

    @NotNull
    private UUID formId;

}
