package altn.booking.assignment.booking.repository.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity(name = "ROOM_AVAILABILITY")
public class RoomAvailability {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "AVAILABILITY_DATE", unique=true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime availabilityDate;

    @OneToOne(mappedBy = "availability")
    @PrimaryKeyJoinColumn
    private Booking booking;
}
