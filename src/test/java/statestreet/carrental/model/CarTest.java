package statestreet.carrental.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CarTest {
    @Test
    @DisplayName("Should create car with valid parameters")
    void shouldCreateCarWithValidParameters() {
        Car car = new Car("CAR001", CarType.SEDAN, "Toyota Camry");

        assertEquals("CAR001", car.getId());
        assertEquals(CarType.SEDAN, car.getType());
        assertEquals("Toyota Camry", car.getModel());
    }

    @Test
    @DisplayName("Should reject null parameters")
    void shouldRejectNullParameters() {
        assertThrows(NullPointerException.class, () ->
                new Car(null, CarType.SEDAN, "Toyota Camry"));

        assertThrows(NullPointerException.class, () ->
                new Car("CAR001", null, "Toyota Camry"));

        assertThrows(NullPointerException.class, () ->
                new Car("CAR001", CarType.SEDAN, null));
    }
}
