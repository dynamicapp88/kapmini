package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResourceDTO {

    private UUID resourceId;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private String description;

}
