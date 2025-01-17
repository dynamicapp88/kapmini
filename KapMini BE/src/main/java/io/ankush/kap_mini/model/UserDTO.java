package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private UUID userId;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    private Isactive isActive;

    @NotNull
    @Size(max = 255)
    private String password;

    private LocalDateTime deletedAt;

    @NotNull
    private Status status;

    @NotNull
    private UUID subdomainID;

}
