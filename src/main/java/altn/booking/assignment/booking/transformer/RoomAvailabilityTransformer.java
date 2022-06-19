package altn.booking.assignment.booking.transformer;

import altn.booking.assignment.booking.model.response.RoomAvailabilityResponse;
import altn.booking.assignment.booking.repository.entity.RoomAvailability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RoomAvailabilityTransformer {

    private RoomAvailabilityTransformer() {
        throw new IllegalStateException("Utility class");
    }

    public static RoomAvailabilityResponse fromRoomAvailabilityEntityList(List<RoomAvailability> availabilityList) {
        List<LocalDateTime> dates = availabilityList
                .stream()
                .map(RoomAvailability::getAvailabilityDate)
                .collect(Collectors.toList());

        return RoomAvailabilityResponse.builder().dates(dates).build();
    }
}
