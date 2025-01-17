package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WorkflowStepDTO {

    private UUID workflowStepId;

    @NotNull
    private StepType stepType;

    @Size(max = 255)
    private String condition;

    @NotNull
    private ActionType actionType;

    @Size(max = 255)
    private String actionDetails;

    private LocalDateTime deleteAt;

    @NotNull
    private UUID workflowId;

}
