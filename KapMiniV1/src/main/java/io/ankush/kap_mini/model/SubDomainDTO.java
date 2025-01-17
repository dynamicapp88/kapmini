package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SubDomainDTO {

    private UUID subdomain;

    @NotNull
    @Size(max = 255)
    private String name;

    private LocalDateTime deleteAt;

}
