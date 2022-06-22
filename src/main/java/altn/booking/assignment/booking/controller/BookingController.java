package altn.booking.assignment.booking.controller;

import altn.booking.assignment.booking.model.request.BookingRequest;
import altn.booking.assignment.booking.model.response.BookingResponse;
import altn.booking.assignment.booking.model.response.RoomAvailabilityResponse;
import altn.booking.assignment.booking.service.BookingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @ApiOperation(value = "Get the room availability", response = RoomAvailabilityResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the availability"),
    })
    @GetMapping("/availability")
    public ResponseEntity<RoomAvailabilityResponse> getRoomAvailability() {
        RoomAvailabilityResponse availability = bookingService.getAvailability();

        return ResponseEntity.ok(availability);
    }

    @ApiOperation(value = "Place a booking specifying a list of dates", response = BookingResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully booked the room"),
            @ApiResponse(code = 400, message = "Breaking the business rules about the dates or maximum of days to book ")
    })
    @PostMapping("/place")
    public ResponseEntity<BookingResponse> placeBooking(@RequestBody BookingRequest request) {
        BookingResponse response = bookingService.placeBooking(request);

        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Modify a booking specifying a list of new dates", response = BookingResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully modified the booking the room"),
            @ApiResponse(code = 400, message = "Breaking the business rules about the dates or maximum of days to book ")
    })
    @PutMapping("/modify/{transactionId}")
    public ResponseEntity<BookingResponse> modifyBookingByTransactionId(
            @RequestBody BookingRequest request,
            @PathVariable String transactionId) {
        BookingResponse response = bookingService.modifyBooking(request, transactionId);

        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Cancel a booking specifying the transactionId", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully canceled the booking the room"),
            @ApiResponse(code = 404, message = "The booking wasn't found")
    })
    @DeleteMapping("/cancel/{transactionId}")
    public ResponseEntity<String> cancelBookingByTransactionId(@PathVariable String transactionId){
        String response = bookingService.cancelBooking(transactionId);

        return ResponseEntity.ok(response);
    }
}
