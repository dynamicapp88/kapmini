package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FormDTO {

    private UUID formId;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private String description;

    private LocalDateTime deleteAt;

    @NotNull
    private UUID appID;

}
