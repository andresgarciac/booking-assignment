package altn.booking.assignment.booking.exception;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
