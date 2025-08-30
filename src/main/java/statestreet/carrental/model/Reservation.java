package statestreet.carrental.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Reservation {
    private final String id;
    private final String customerId;
    private final Car car;
    private final LocalDateTime startDateTime;
    private int durationInDays;
    private final LocalDateTime createdAt;

    public LocalDateTime getEndDateTime() {
        return startDateTime.plusDays(durationInDays);
    }
}
