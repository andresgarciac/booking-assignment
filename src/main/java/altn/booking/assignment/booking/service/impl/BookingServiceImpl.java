package altn.booking.assignment.booking.service.impl;

import altn.booking.assignment.booking.exception.BookingNotFoundException;
import altn.booking.assignment.booking.exception.NotValidBookingException;
import altn.booking.assignment.booking.model.request.BookingRequest;
import altn.booking.assignment.booking.model.response.BookingResponse;
import altn.booking.assignment.booking.model.response.RoomAvailabilityResponse;
import altn.booking.assignment.booking.repository.BookingRepository;
import altn.booking.assignment.booking.repository.RoomAvailabilityRepository;
import altn.booking.assignment.booking.repository.entity.Booking;
import altn.booking.assignment.booking.repository.entity.RoomAvailability;
import altn.booking.assignment.booking.service.BookingService;
import altn.booking.assignment.booking.transformer.RoomAvailabilityTransformer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomAvailabilityRepository availabilityRepository;

    @Value("${booking.max-days-to-book}")
    private int maxDaysToBook;

    @Value("${booking.days-in-advance-to-book}")
    private int daysInAdvanceToBook;

    public BookingServiceImpl(BookingRepository bookingRepository, RoomAvailabilityRepository availabilityRepository) {
        this.bookingRepository = bookingRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public RoomAvailabilityResponse getAvailability() {
        List<RoomAvailability> availability = availabilityRepository.findByBookingReservationDateIdIsNull();
        return RoomAvailabilityTransformer.fromRoomAvailabilityEntityList(availability);
    }

    @Transactional
    public BookingResponse placeBooking(BookingRequest request) {
        String transactionId = UUID.randomUUID().toString();

        if (request.getDates().size() > maxDaysToBook) {
            throw new NotValidBookingException("You can't book more than " + maxDaysToBook + " days");
        }

        List<Booking> bookings = request.getDates().stream().map(date -> {
            String dateId = bookingDateValidations(date);

            return Booking.builder()
                    .transactionId(transactionId)
                    .reservationDateId(dateId)
                    .userId(request.getUserId())
                    .build();
        }).collect(Collectors.toList());

        bookingRepository.saveAll(bookings);

        return BookingResponse.builder()
                .transactionId(transactionId)
                .dates(request.getDates())
                .userId(request.getUserId())
                .build();
    }

    @Transactional
    public BookingResponse modifyBooking(BookingRequest request, String transactionId) {
        List<Booking> newBookingDates = request.getDates().stream().map(date -> {
            String dateId = bookingDateValidations(date);
            checkBookingDateStatus(request, date, dateId);

            return Booking.builder()
                    .transactionId(transactionId)
                    .reservationDateId(dateId)
                    .userId(request.getUserId())
                    .build();
        }).collect(Collectors.toList());

        List<Booking> bookedDates = bookingRepository.findByTransactionId(transactionId);

        newDatesToBook(newBookingDates.stream(), bookedDates);
        oldDatesToDelete(newBookingDates, bookedDates.stream());

        return BookingResponse.builder()
                .transactionId(transactionId)
                .dates(request.getDates())
                .userId(request.getUserId())
                .build();
    }

    @Transactional
    public String cancelBooking(String transactionId) {
        List<Booking> bookingToCancel = bookingRepository.findByTransactionId(transactionId);

        if (bookingToCancel.isEmpty()) {
            throw new BookingNotFoundException("The booking to cancel doesn't exist");
        }

        bookingRepository.deleteAll(bookingToCancel);

        return transactionId;
    }

    private void oldDatesToDelete(List<Booking> bookings, Stream<Booking> bookedDates) {
        List<Booking> datesToDelete = bookedDates.filter(b ->
                bookings.stream().noneMatch(p -> p.getReservationDateId().equals(b.getReservationDateId()))
        ).collect(Collectors.toList());

        bookingRepository.deleteAll(datesToDelete);
    }

    private void newDatesToBook(Stream<Booking> bookings, List<Booking> bookedDates) {
        List<Booking> datesToCreate = bookings.filter(b ->
                bookedDates.stream().noneMatch(p -> p.getReservationDateId().equals(b.getReservationDateId()))
        ).collect(Collectors.toList());

        bookingRepository.saveAll(datesToCreate);
    }

    private void checkBookingDateStatus(BookingRequest request, LocalDateTime date, String dateId) {
        Optional<Booking> dateBooked = bookingRepository
                .findByReservationDateIdAndUserIdIsNot(dateId, request.getUserId());

        if(dateBooked.isPresent()) {
            throw new NotValidBookingException("The date: " + date + " was already reserved by another user");
        }
    }

    private String bookingDateValidations(final LocalDateTime date) {
        if (date.isBefore(LocalDateTime.now()) || date.isEqual(LocalDateTime.now())) {
            throw new NotValidBookingException("The date " + date + " is not valid to place a booking");
        }
        else if (date.isAfter(LocalDateTime.now().plusDays(daysInAdvanceToBook))) {
            throw new NotValidBookingException("The date " + date + " is " + daysInAdvanceToBook + " days after");
        }

        Optional<RoomAvailability> availability = availabilityRepository.findByAvailabilityDate(date);

        return availability
                .orElseThrow(() -> new NotValidBookingException("Date no available to place a booking"))
                .getId();
    }
}
