package altn.booking.assignment.booking.repository.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class BookingId implements Serializable {
    private String transactionId;

    private String reservationDateId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingId bookingId = (BookingId) o;
        return Objects.equals(transactionId, bookingId.transactionId) && Objects.equals(reservationDateId, bookingId.reservationDateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, reservationDateId);
    }
}