package statestreet.carrental.storage;

import statestreet.carrental.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationStorage {
    void save(Reservation reservation);

    List<Reservation> findAllReservations();

    boolean hasConflictReservation(String carId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
