package statestreet.carrental.service;

import lombok.RequiredArgsConstructor;
import statestreet.carrental.model.Car;
import statestreet.carrental.model.CarType;
import statestreet.carrental.model.Reservation;
import statestreet.carrental.storage.CarStorage;
import statestreet.carrental.storage.ReservationStorage;
import statestreet.carrental.validation.ReservationRequestValidator;
import statestreet.carrental.exception.CarNotAvailableException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CarRentalService {
    private final CarStorage carStorage;
    private final ReservationStorage reservationStorage;
    private final ReservationRequestValidator reservationRequestValidator;

    public Reservation reserveCar(CarType carType, String customerId, LocalDateTime startDateTime,
                                  int durationOfReservation) {
        reservationRequestValidator.validateInput(carType, customerId, startDateTime, durationOfReservation);
        Car avalableCar = findAvalableCar(carType, startDateTime, durationOfReservation);
        Reservation reservation = createReservation(customerId, avalableCar, startDateTime, durationOfReservation);
        reservationStorage.save(reservation);
        return reservation;
    }

    private Reservation createReservation(String customerId, Car avalableCar, LocalDateTime startDateTime,
                                          int durationOfReservation) {
        return Reservation.builder()
                .id(generateId())
                .customerId(customerId)
                .car(avalableCar)
                .startDateTime(startDateTime)
                .durationInDays(durationOfReservation)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Car findAvalableCar(CarType carType, LocalDateTime startDateTime, int durationOfReservation) {
        LocalDateTime endDateTime = startDateTime.plusDays(durationOfReservation);
        List<Car> availableCarsByType = carStorage.findAvailableCarsByType(carType);
        if (availableCarsByType.isEmpty()) {
            throw new CarNotAvailableException("There are no cars of type %s available"
                    .formatted(carType.getDisplayName()));
        }
        return availableCarsByType.stream()
                .filter(car -> !reservationStorage.hasConflictReservation(car.getId(), startDateTime, endDateTime))
                .findFirst()//TODO: dodac zeby zwracało liste?
                .orElseThrow(() -> new CarNotAvailableException(
                        "There are no cars of type %s available for provided time period"
                                .formatted(carType.getDisplayName())));
    }

    private String generateId() {
        return "Reservation" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
