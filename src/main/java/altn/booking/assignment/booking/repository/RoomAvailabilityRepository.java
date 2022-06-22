package altn.booking.assignment.booking.repository;

import altn.booking.assignment.booking.repository.entity.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomAvailabilityRepository  extends JpaRepository<RoomAvailability, String> {

    List<RoomAvailability> findByBookingReservationDateIdIsNullAndAvailabilityDateAfter(LocalDateTime sDate);
    Optional<RoomAvailability> findByAvailabilityDate(LocalDateTime date);
}
