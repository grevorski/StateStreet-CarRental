package statestreet.carrental.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CarTest {
    @Test
    @DisplayName("Should create car with valid parameters")
    void shouldCreateCarWithValidParameters() {
        Car car = Car.builder()
                .id("CAR001")
                .type(CarType.SEDAN)
                .model("Toyota Camry")
                .build();

        assertEquals("CAR001", car.getId());
        assertEquals(CarType.SEDAN, car.getType());
        assertEquals("Toyota Camry", car.getModel());
    }

    @Test
    @DisplayName("Should reject null parameters")
    void shouldRejectNullParameters() {
        assertThrows(NullPointerException.class, () -> Car.builder()
                .id(null)
                .type(CarType.SEDAN)
                .model("Toyota Camry")
                .build());

        assertThrows(NullPointerException.class, () ->Car.builder()
                .id("CAR001")
                .type(null)
                .model("Toyota Camry")
                .build());

        assertThrows(NullPointerException.class, () ->Car.builder()
                .id("CAR001")
                .type(CarType.SEDAN)
                .model(null)
                .build());
    }
}
