package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FieldDataDTO {

    private UUID fieldDataId;

    @NotNull
    @Size(max = 255)
    private String value;

    private LocalDateTime deleteAt;

    @NotNull
    private UUID fieldId;

    @NotNull
    private UUID recordId;

}
