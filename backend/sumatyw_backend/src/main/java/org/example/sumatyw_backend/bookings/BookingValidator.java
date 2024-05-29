package org.example.sumatyw_backend.bookings;


import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
@EnableScheduling
public class BookingValidator {

    private BookingService bookingService;
    private BookingRepository bookingRepository;

    @Scheduled(fixedRate = 60000)
    public void validateBookings() {

        LocalDateTime currentTime = LocalDateTime.now();
        List<Booking> allBookings = bookingService.getAllActiveBookings();

        for(Booking booking : allBookings) {

            if(booking.getTimestamp().plusHours(1).isBefore(currentTime)) {
                booking.setStatus(Status.OutOfDate);
            }
        }
        bookingRepository.saveAll(allBookings);
    }
}
