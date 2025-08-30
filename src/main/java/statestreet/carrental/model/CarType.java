package statestreet.carrental.model;

import lombok.Getter;

@Getter
public enum CarType {
    SEDAN("Sedan"),
    SUV("SUV"),
    VAN("Van");

    private final String displayName;

    CarType(String displayName) {
        this.displayName = displayName;
    }
}
