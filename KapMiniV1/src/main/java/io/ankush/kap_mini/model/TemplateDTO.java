package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TemplateDTO {

    private UUID templateId;

    @NotNull
    @Size(max = 255)
    private String templateName;

    @NotNull
    private String description;

    @NotNull
    private String templateData;

}
