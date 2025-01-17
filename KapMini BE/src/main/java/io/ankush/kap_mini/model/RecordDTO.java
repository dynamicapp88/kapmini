package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RecordDTO {

    private UUID recordId;

    @NotNull
    @Size(max = 255)
    private String status;

    @NotNull
    private UUID formId;

    @NotNull
    private UUID createdBy;

    @NotNull
    private UUID updatedBy;

}
