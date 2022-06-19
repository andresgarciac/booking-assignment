package altn.booking.assignment.booking.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class BookingRequest {

    private List<LocalDateTime> dates;
    private String userId;
}
