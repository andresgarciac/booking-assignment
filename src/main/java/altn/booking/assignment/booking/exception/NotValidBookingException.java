package altn.booking.assignment.booking.exception;

public class NotValidBookingException extends RuntimeException {

    public NotValidBookingException(String errorMessage) {
        super(errorMessage);
    }
}
