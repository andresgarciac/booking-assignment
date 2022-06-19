package altn.booking.assignment.booking.controller;

import altn.booking.assignment.booking.exception.BookingNotFoundException;
import altn.booking.assignment.booking.exception.NotValidBookingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookingExceptionController {

    @ExceptionHandler(value = NotValidBookingException.class)
    public ResponseEntity<String> notValidBookingExceptionHandler(NotValidBookingException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(value = BookingNotFoundException.class)
    public ResponseEntity<String> bookingNotFoundExceptionHandler(BookingNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
