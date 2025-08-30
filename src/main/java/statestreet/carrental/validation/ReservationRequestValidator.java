package statestreet.carrental.validation;

import statestreet.carrental.model.CarType;
import statestreet.carrental.exception.InvalidReservationException;

import java.time.LocalDateTime;

public class ReservationRequestValidator {
    public void validateInput(CarType carType, String customerId, LocalDateTime startDateTime,
                              int durationInDays) {
        if (carType == null) {
            throw new InvalidReservationException("Invalid Car type. Cannot be empty");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new InvalidReservationException("Invalid Customer ID. Cannot be empty");
        }
        if (startDateTime == null) {
            throw new InvalidReservationException("Invalid Start Date of reservation. Cannot be empty");
        }
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException("Invalid Start Date of reservation. Provided start date is from past");
        }
        if (durationInDays <= 0) {
            throw new InvalidReservationException("Invalid duration of reservation. Must be positive number");
        }
        if (durationInDays > 365) {
            throw new InvalidReservationException("Invalid duration of reservation. Maximum period is 365 days");
        }
    }
}
