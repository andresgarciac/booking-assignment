package altn.booking.assignment.booking.controller;

import altn.booking.assignment.booking.model.request.BookingRequest;
import altn.booking.assignment.booking.model.response.BookingResponse;
import altn.booking.assignment.booking.model.response.RoomAvailabilityResponse;
import altn.booking.assignment.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/availability")
    public ResponseEntity<RoomAvailabilityResponse> getRoomAvailability() {
        RoomAvailabilityResponse availability = bookingService.getAvailability();

        return ResponseEntity.ok(availability);
    }

    @PostMapping("/place/booking")
    public ResponseEntity<BookingResponse> placeBooking(@RequestBody BookingRequest request) {
        BookingResponse response = bookingService.placeBooking(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/modify/booking/{transactionId}")
    public ResponseEntity<BookingResponse> modifyBookingByTransactionId(
            @RequestBody BookingRequest request,
            @PathVariable String transactionId) {
        BookingResponse response = bookingService.modifyBooking(request, transactionId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cancel/booking/{transactionId}")
    public ResponseEntity<String> cancelBookingByTransactionId(@PathVariable String transactionId){
        String response = bookingService.deleteBooking(transactionId);

        return ResponseEntity.ok(response);
    }
}
