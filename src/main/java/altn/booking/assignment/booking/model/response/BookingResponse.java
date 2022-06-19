package altn.booking.assignment.booking.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class BookingResponse {
    private String transactionId;
    private List<LocalDateTime> dates;
    private String userId;
}
