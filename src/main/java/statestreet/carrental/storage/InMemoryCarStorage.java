package statestreet.carrental.storage;

import statestreet.carrental.model.Car;
import statestreet.carrental.model.CarType;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCarStorage implements CarStorage {
    private final List<Car> cars = new ArrayList<>();

    public InMemoryCarStorage() {
        initializeCars();
    }

    private void initializeCars() {
        addCar("S001", CarType.SEDAN, "Toyota Corolla");
        addCar("S002", CarType.SEDAN, "Volkswagen Passat");
        addCar("U001", CarType.SUV, "Ford Explorer");
        addCar("V001", CarType.VAN, "Ford Transit");
        addCar("S003", CarType.SEDAN, "Dodge Challenger");
    }

    private void addCar(String id, CarType carType, String model) {
        Car car = Car.builder()
                .id(id)
                .type(carType)
                .model(model)
                .build();
        cars.add(car);
    }

    @Override
    public List<Car> findAvailableCarsByType(CarType carType) {
        return cars.stream()
                .filter(car -> car.getType().equals(carType) && car.isAvailable())
                .toList();
    }

    @Override
    public List<Car> findAllCars() {
        return cars;
    }
}
