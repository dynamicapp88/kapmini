package io.ankush.kap_mini.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FieldDTO {

    private UUID fieldId;

    @NotNull
    @Size(max = 255)
    private String fieldName;

    @NotNull
    private FieldType fieldType;

    @NotNull
    @Size(max = 255)
    private String defaultValue;

    @Size(max = 255)
    private String validationRules;

    @NotNull
    @JsonProperty("isRequired")
    private Boolean isRequired;

    @Size(max = 255)
    private String deleteAt;

    @NotNull
    private UUID formId;

}
