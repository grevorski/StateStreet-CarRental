package statestreet.carrental.model;

import lombok.*;

@Data
@Builder
public class Car {
    private final String id;
    private final CarType type;
    private final String model;
    @Builder.Default
    private boolean available = true;
}
