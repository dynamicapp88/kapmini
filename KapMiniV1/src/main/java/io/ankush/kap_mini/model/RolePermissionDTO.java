package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RolePermissionDTO {

    private UUID rolePermissionId;

    @NotNull
    private UUID roleId;

    @NotNull
    private UUID premissionId;

}
