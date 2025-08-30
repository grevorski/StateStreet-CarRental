package statestreet.carrental.service;

import lombok.RequiredArgsConstructor;
import statestreet.carrental.model.Car;
import statestreet.carrental.model.CarType;
import statestreet.carrental.model.Reservation;
import statestreet.carrental.storage.CarStorage;
import statestreet.carrental.storage.ReservationStorage;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CarRentalService {
    private final CarStorage carStorage;
    private final ReservationStorage reservationStorage;

    public Reservation reserveCar(CarType carType, String customerId, LocalDateTime startDateTime, int durationOfReservation) {
        //validacja inputu usera
        Car avalableCar = findAvalableCar(carType, startDateTime, durationOfReservation);
        Reservation reservation = createReservation(customerId, avalableCar, startDateTime, durationOfReservation);
        reservationStorage.save(reservation);
        return reservation;
    }

    private Reservation createReservation(String customerId, Car avalableCar, LocalDateTime startDateTime, int durationOfReservation) {
        return Reservation.builder()
                .id("randomIdForNow") //TODO: generowanie id? normalnie zrobilby to triger na bazie
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
            //TODO: strowowac exceptiona z informacja o braku auta dla danej daty
            throw new RuntimeException("No car");
        }
        return availableCarsByType.stream()
                .filter(car -> !reservationStorage.hasConflictReservation(car.getId(), startDateTime, endDateTime))
                .findFirst()//TODO: dodac zeby zwracało liste?
                .orElseThrow(() -> new RuntimeException("No car"));//TODO: exception customowy
    }
}
