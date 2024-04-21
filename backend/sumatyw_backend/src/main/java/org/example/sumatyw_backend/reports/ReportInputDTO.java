package org.example.sumatyw_backend.reports;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ReportInputDTO(
    @NotNull(message = "User id cannot be null")
    UUID userId,
    @NotNull(message = "Restaurant id cannot be null")
    UUID restaurantId,
    @NotBlank(message = "Report cause cannot be blank")
    @Size(max = 500, message = "Report cause cannot contain more than 500 characters")
    String cause
) {
}
