package altn.booking.assignment.booking.repository;

import altn.booking.assignment.booking.repository.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, String> {

    Optional<Booking> findByReservationDateIdAndUserIdIsNot(String dateId, String userId);
    List<Booking> findByTransactionId(String transactionId);
}
