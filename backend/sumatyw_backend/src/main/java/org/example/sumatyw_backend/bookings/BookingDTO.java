package org.example.sumatyw_backend.bookings;

import java.util.UUID;

public record BookingDTO (
    UUID bookedId,
    String timestamp,
    String picked_up_timestamp,
    UUID userId
){
}
