package altn.booking.assignment.booking.repository.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Entity(name = "BOOKINGS")
@IdClass(BookingId.class)
public class Booking {

    @Id
    @Column(name = "TRANSACTION_ID")
    private String transactionId;

    @Id
    @Column(name = "reservation_date_id")
    private String reservationDateId;

    @OneToOne
    @JoinColumn(name = "reservation_date_id")
    @MapsId
    private RoomAvailability availability;

    @Column(name = "USER_ID")
    private String userId;

}
