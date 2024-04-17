package org.example.sumatyw_backend.reports;

import java.util.UUID;

public record ReportInputDTO(UUID userId,UUID restaurantId,String cause) {
}
