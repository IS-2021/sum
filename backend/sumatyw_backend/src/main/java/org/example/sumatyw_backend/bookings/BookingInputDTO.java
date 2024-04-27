package org.example.sumatyw_backend.bookings;
import java.util.UUID;

public record BookingInputDTO (
    String timestamp,
    String picked_up_timestamp,
    UUID userId
){
}
