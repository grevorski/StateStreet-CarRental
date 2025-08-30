package statestreet.carrental.storage;

import statestreet.carrental.model.Reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryReservationStorage implements ReservationStorage {
    private final List<Reservation> reservationList = new ArrayList<>();

    @Override
    public void save(Reservation reservation) {
        reservationList.add(reservation);
    }

    @Override
    public List<Reservation> findAllReservations() {
        return reservationList;
    }

    @Override
    public boolean hasConflictReservation(String carId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return reservationList.stream()
                .filter(reservation -> reservation.getCar().getId().equals(carId))
                .anyMatch(reservation -> reservation.getStartDateTime().isBefore(endDateTime) &&
                        startDateTime.isBefore(reservation.getEndDateTime()));
    }
}
