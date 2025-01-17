package io.ankush.kap_mini.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ApiResponse {

    @NotNull
    @Size(max = 255)
    private String status;

    @NotNull
    @Size(max = 255)
    private String data;

    @NotNull
    @Size(max = 255)
    private String message;

    @Size(max = 255)
    private String error;

    @NotNull
    private LocalDateTime timestamp;

}
