package org.example.sumatyw_backend.reports;

import java.util.UUID;

public record ReportDTO(
    UUID id,
    UUID userId,
    UUID restaurantId,
    String cause,
    String timestamp,
    boolean isOpen
) {
}
