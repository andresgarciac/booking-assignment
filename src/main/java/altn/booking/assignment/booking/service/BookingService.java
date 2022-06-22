package altn.booking.assignment.booking.service;

import altn.booking.assignment.booking.model.request.BookingRequest;
import altn.booking.assignment.booking.model.response.BookingResponse;
import altn.booking.assignment.booking.model.response.RoomAvailabilityResponse;

public interface BookingService {
    RoomAvailabilityResponse getAvailability();
    BookingResponse placeBooking(BookingRequest request);
    BookingResponse modifyBooking(BookingRequest request, String transactionId);
    String cancelBooking(String transactionId);
}
