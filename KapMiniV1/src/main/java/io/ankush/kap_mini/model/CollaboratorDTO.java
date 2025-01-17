package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CollaboratorDTO {

    private UUID collaboratorID;

    private LocalDateTime deleteAt;

    @NotNull
    private UUID userID;

    @NotNull
    private UUID subdomainId;

}
