package statestreet.carrental.storage;

import statestreet.carrental.model.Car;
import statestreet.carrental.model.CarType;

import java.util.List;

public interface CarStorage {
    List<Car> findAvailableCarsByType(CarType carType);
    List<Car> findAllCars();
}
