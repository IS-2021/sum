package org.example.sumatyw_backend.bookings;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private BookingService bookingService;


    @PostMapping()
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingInputDTO bookingInputDTO) {

      Booking booking = bookingService.createBooking(BookingDTOMapper.mapBookingInputDTOToBooking(bookingInputDTO));

    return new ResponseEntity<>(BookingDTOMapper.mapBookingToBookingDTO(booking), HttpStatus.OK);
    }
}
