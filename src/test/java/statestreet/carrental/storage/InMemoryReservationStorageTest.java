package statestreet.carrental.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import statestreet.carrental.model.Car;
import statestreet.carrental.model.CarType;
import statestreet.carrental.model.Reservation;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryReservationStorageTest {
    private InMemoryReservationStorage reservationStorage;
    private Car testCar;

    @BeforeEach
    void setUp() {
        reservationStorage = new InMemoryReservationStorage();
        testCar = Car.builder()
                .id("TEST-CAR-001")
                .type(CarType.SEDAN)
                .model("Test Car")
                .build();
    }

    @Test
    @DisplayName("Should detect overlapping reservations - start overlap")
    void shouldDetectOverlappingReservationsStartOverlap() {
        // Given
        LocalDateTime existingStart = LocalDateTime.now();

        Reservation existingReservation = Reservation.builder()
                .id("EXISTING-RES")
                .customerId("CUSTOMER-1")
                .car(testCar)
                .startDateTime(existingStart)
                .durationInDays(5)
                .createdAt(LocalDateTime.now())
                .build();

        reservationStorage.save(existingReservation);

        LocalDateTime newStart = LocalDateTime.now().plusDays(2);
        LocalDateTime newEnd = newStart.plusDays(5);

        // Then
        boolean hasConflict = reservationStorage.hasConflictReservation(
                testCar.getId(), newStart, newEnd);
        assertThat(hasConflict).isTrue();
    }

    @Test
    @DisplayName("Should detect overlapping reservations - end overlap")
    void shouldDetectOverlappingReservationsEndOverlap() {
        // Given
        LocalDateTime existingStart = LocalDateTime.now().plusDays(10);

        Reservation existingReservation = Reservation.builder()
                .id("EXISTING-RES")
                .customerId("CUSTOMER-1")
                .car(testCar)
                .startDateTime(existingStart)
                .durationInDays(5)
                .createdAt(LocalDateTime.now())
                .build();

        reservationStorage.save(existingReservation);

        LocalDateTime newStart = LocalDateTime.now().plusDays(7);
        LocalDateTime newEnd = newStart.plusDays(5);

        // Then
        boolean hasConflict = reservationStorage.hasConflictReservation(
                testCar.getId(), newStart, newEnd);
        assertThat(hasConflict).isTrue();
    }

    @Test
    @DisplayName("Should NOT detect conflict for non-overlapping reservations")
    void shouldNotDetectConflictForNonOverlappingReservations() {
        // Given
        LocalDateTime existingStart = LocalDateTime.now().plusDays(10);

        Reservation existingReservation = Reservation.builder()
                .id("EXISTING-RES")
                .customerId("CUSTOMER-1")
                .car(testCar)
                .startDateTime(existingStart)
                .durationInDays(5)
                .createdAt(LocalDateTime.now())
                .build();

        reservationStorage.save(existingReservation);

        LocalDateTime newStart = LocalDateTime.now();
        LocalDateTime newEnd = newStart.plusDays(5);

        // Then
        boolean hasConflict = reservationStorage.hasConflictReservation(
                testCar.getId(), newStart, newEnd);
        assertThat(hasConflict).isFalse();
    }
}