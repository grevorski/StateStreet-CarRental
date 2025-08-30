package statestreet.carrental;

import statestreet.carrental.model.Car;
import statestreet.carrental.model.CarType;
import statestreet.carrental.model.Reservation;
import statestreet.carrental.service.CarRentalService;
import statestreet.carrental.storage.InMemoryCarStorage;
import statestreet.carrental.storage.InMemoryReservationStorage;
import statestreet.carrental.validation.ReservationRequestValidator;

import java.time.LocalDateTime;
import java.util.Scanner;

public class CarRentalApplication {
    public static void main(String[] args) {
        InMemoryCarStorage carStorage = new InMemoryCarStorage();
        InMemoryReservationStorage reservationStorage = new InMemoryReservationStorage();
        CarRentalService service = new CarRentalService(
                carStorage, reservationStorage, new ReservationRequestValidator());

        systemPresentation(service, carStorage);
    }

    private static void systemPresentation(CarRentalService service, InMemoryCarStorage carStorage) {
        try {
            System.out.println("Available cars:");
            System.out.println(carStorage.findAllCars());
            for (Car car : carStorage.findAllCars()) {
                System.out.println(car);
            }
            Scanner inputScanner = new Scanner(System.in);
            System.out.println("Provide carType from: SEDAN, SUV, VAN");
            String carType = inputScanner.next();
            System.out.println("Provide year");
            int year = inputScanner.nextInt();
            System.out.println("Provide month");
            int month = inputScanner.nextInt();
            System.out.println("Provide day");
            int day = inputScanner.nextInt();
            System.out.println("Provide hour");
            int hour = inputScanner.nextInt();
            System.out.println("Provide minute");
            int minute = inputScanner.nextInt();
            LocalDateTime startOfReservation = LocalDateTime.of(year, month, day, hour, minute);
            System.out.println("Provide duration");
            int duration = inputScanner.nextInt();

            Reservation reservation = service.reserveCar(CarType.valueOf(carType), "CUSTOMER1", startOfReservation, duration);

            System.out.printf("Reservation created: %s%n", reservation);
        } catch (Exception e) {
            System.err.printf("Error: %s%n", e.getMessage());
        }
    }
}
