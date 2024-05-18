package org.example.sumatyw_backend.bookings;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping()
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllActiveBookings();
        return new ResponseEntity<>(bookings.stream().map(BookingDTOMapper::mapBookingToBookingDTO).toList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable("id") UUID id) {
        Booking booking = bookingService.getBookingById(id);
        return new ResponseEntity<>(BookingDTOMapper.mapBookingToBookingDTO(booking), HttpStatus.OK);
    }

    @GetMapping(params = {"userId"})
    public ResponseEntity<BookingDTO> getBookingByUserId(@RequestParam("userId") UUID userId) {
        Booking booking = bookingService.getBookingByUserId(userId);
        return new ResponseEntity<>(BookingDTOMapper.mapBookingToBookingDTO(booking), HttpStatus.OK);
    }

    @GetMapping(params = {"restaurantId"})
    public ResponseEntity<List<BookingDTO>> getBookingsByRestaurantId(@RequestParam("restaurantId") UUID restaurandId) {
        List<Booking> bookings = bookingService.getBookingsByRestaurantID(restaurandId);
        return new ResponseEntity<>(bookings.stream().map(BookingDTOMapper::mapBookingToBookingDTO).toList(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> markBookingAsPickedUp(@PathVariable("id") UUID id, @RequestBody BookingInputDTO bookingInputDTO) {
        Booking booking = bookingService.markBookingAsPickedUp(id, BookingDTOMapper.mapBookingInputDTOToBooking(bookingInputDTO));
        return new ResponseEntity<>(BookingDTOMapper.mapBookingToBookingDTO(booking), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable("id") UUID id) {
        bookingService.deleteBookingById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
