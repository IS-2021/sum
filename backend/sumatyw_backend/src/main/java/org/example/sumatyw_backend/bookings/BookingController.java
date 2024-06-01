package org.example.sumatyw_backend.bookings;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private BookingService bookingService;


    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingInputDTO bookingInputDTO) {
        Booking booking = bookingService.createBooking(BookingDTOMapper.mapBookingInputDTOToBooking(bookingInputDTO));
        return new ResponseEntity<>(BookingDTOMapper.mapBookingToBookingDTO(booking), HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllActiveBookings();
        return new ResponseEntity<>(bookings.stream().map(BookingDTOMapper::mapBookingToBookingDTO).toList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER, RESTAURANT')")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable("id") UUID id) {
        Booking booking = bookingService.getBookingById(id);
        return new ResponseEntity<>(BookingDTOMapper.mapBookingToBookingDTO(booking), HttpStatus.OK);
    }

    @GetMapping( value = "/active", params = {"userId"})
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BookingDTO> getBookingByUserId(@RequestParam("userId") UUID userId) {
        Booking booking = bookingService.getBookingByUserId(userId);
        return new ResponseEntity<>(BookingDTOMapper.mapBookingToBookingDTO(booking), HttpStatus.OK);
    }

    @GetMapping(params = {"userId"})
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BookingDTO>> getAllBookingsByUserId(@RequestParam("userId") UUID userId) {
        List<Booking> userBookings = bookingService.getAllUserBookings(userId);

        return new ResponseEntity<>(userBookings.stream().map(BookingDTOMapper::mapBookingToBookingDTO).toList(), HttpStatus.OK);
    }

    @GetMapping(params = {"restaurantId"})
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<List<BookingDTO>> getBookingsByRestaurantId(@RequestParam("restaurantId") UUID restaurandId) {
        List<Booking> bookings = bookingService.getBookingsByRestaurantID(restaurandId);
        return new ResponseEntity<>(bookings.stream().map(BookingDTOMapper::mapBookingToBookingDTO).toList(), HttpStatus.OK);
    }

    @GetMapping(params = {"restaurantId","active"})
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<List<BookingDTO>> getActiveBookingsByRestaurantId(@RequestParam("restaurantId") UUID restaurandId, @RequestParam("active") boolean active) {
        List<Booking> bookings = bookingService.getActiveBookingsByRestaurantID(restaurandId,active);
        return new ResponseEntity<>(bookings.stream().map(BookingDTOMapper::mapBookingToBookingDTO).toList(), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RESTAURANT')")
    public ResponseEntity<BookingDTO> markBookingAsPickedUp(@PathVariable("id") UUID id, @RequestBody BookingInputDTO bookingInputDTO) {
        Booking booking = bookingService.markBookingAsPickedUp(id, BookingDTOMapper.mapBookingInputDTOToBooking(bookingInputDTO));
        return new ResponseEntity<>(BookingDTOMapper.mapBookingToBookingDTO(booking), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BookingDTO> cancelBookingById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(BookingDTOMapper.mapBookingToBookingDTO(bookingService.cancelBookingById(id)),HttpStatus.OK);
    }
}
