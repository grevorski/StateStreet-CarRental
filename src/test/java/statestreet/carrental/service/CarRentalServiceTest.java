package statestreet.carrental.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import statestreet.carrental.exception.CarNotAvailableException;
import statestreet.carrental.model.Car;
import statestreet.carrental.model.CarType;
import statestreet.carrental.model.Reservation;
import statestreet.carrental.storage.CarStorage;
import statestreet.carrental.storage.ReservationStorage;
import statestreet.carrental.validation.ReservationRequestValidator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarRentalServiceTest {
    @Mock
    private CarStorage carStorage;
    @Mock
    private ReservationStorage reservationStorage;
    @Mock
    private ReservationRequestValidator reservationRequestValidator;
    @InjectMocks
    private CarRentalService carRentalService;

    private Car testCar;
    private LocalDateTime futureDate;

    @BeforeEach
    void setUp() {
        testCar = Car.builder()
                .id("TEST-001")
                .type(CarType.SEDAN)
                .model("Test Car")
                .build();
        futureDate = LocalDateTime.now().plusDays(1);
    }

    @Test
    @DisplayName("Should successfully reserve available car")
    void shouldReserveAvailableCar() {
        // Given
        when(carStorage.findAvailableCarsByType(CarType.SEDAN)).thenReturn(List.of(testCar));
        when(reservationStorage.hasConflictReservation(anyString(), any(), any())).thenReturn(false);

        // When
        Reservation result = carRentalService.reserveCar(CarType.SEDAN, "CUSTOMER-001", futureDate, 3);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo("CUSTOMER-001");
        assertThat(result.getCar()).isEqualTo(testCar);
        assertThat(result.getDurationInDays()).isEqualTo(3);
        assertThat(result.getStartDateTime()).isEqualTo(futureDate);
        assertThat(result.getEndDateTime()).isEqualTo(futureDate.plusDays(3));
        assertThat(result.getId()).isNotNull().startsWith("Reservation");
        assertThat(result.getCreatedAt()).isNotNull();

        verify(reservationStorage).save(result);
        verify(carStorage).findAvailableCarsByType(CarType.SEDAN);
        verify(reservationStorage).hasConflictReservation(eq("TEST-001"), eq(futureDate), eq(futureDate.plusDays(3)));
    }

    @Test
    @DisplayName("Should throw exception when no cars available of requested type")
    void shouldThrowExceptionWhenNoCarsAvailable() {
        // Given
        when(carStorage.findAvailableCarsByType(CarType.SUV)).thenReturn(Collections.emptyList());

        // When & Then
        assertThatThrownBy(() -> carRentalService.reserveCar(CarType.SUV, "CUSTOMER-001", futureDate, 3))
                .isInstanceOf(CarNotAvailableException.class)
                .hasMessageContaining("There are no cars of type SUV available");

        verifyNoInteractions(reservationStorage);
    }

    @Test
    @DisplayName("Should throw exception when cars exist but all have conflicting reservations")
    void shouldThrowExceptionWhenAllCarsHaveConflicts() {
        // Given
        when(carStorage.findAvailableCarsByType(CarType.SEDAN)).thenReturn(List.of(testCar));
        when(reservationStorage.hasConflictReservation(anyString(), any(), any())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> carRentalService.reserveCar(CarType.SEDAN, "CUSTOMER-001", futureDate, 3))
                .isInstanceOf(CarNotAvailableException.class)
                .hasMessageContaining("There are no cars of type Sedan available for provided time period");

        verify(carStorage).findAvailableCarsByType(CarType.SEDAN);
        verify(reservationStorage).hasConflictReservation("TEST-001", futureDate, futureDate.plusDays(3));
        verify(reservationStorage, never()).save(any());
    }
}
