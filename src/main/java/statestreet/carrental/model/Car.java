package statestreet.carrental.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class Car {
    @NonNull
    private final String id;
    @NonNull
    private final CarType type;
    @NonNull
    private final String model;
    @Builder.Default
    private boolean available = true;
}
