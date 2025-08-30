package statestreet.carrental.storage;

import statestreet.carrental.model.Reservation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryReservationStorage implements ReservationStorage {
    private final Map<String, Reservation> reservationMap = new HashMap<>();

    @Override
    public void save(Reservation reservation) {
        reservationMap.put(reservation.getId(), reservation);
    }

    @Override
    public List<Reservation> findAllReservations() {
        return reservationMap.values().stream().toList();
    }
}
