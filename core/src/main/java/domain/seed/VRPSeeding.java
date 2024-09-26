package domain.seed;

import domain.Customer;
import domain.Depot;
import domain.Location;
import domain.Vehicle;

import java.util.Arrays;
import java.util.List;

public class VRPSeeding {
    public static List<Depot> createDepots() {
        return List.of(
            new Depot(1, new Location(0, 0, 5)),
            new Depot(2, new Location(1, 8, 7)),
            new Depot(3, new Location(5, 5, 10)),
            new Depot(4, new Location(3, 2, 8))
        );
    }

    public static List<Customer> createCustomers() {
        return Arrays.asList(
            new Customer(1, new Location(0, 2, 3), 3),
            new Customer(2, new Location(0, 5, 1), 4),
            new Customer(3, new Location(0, 1, 4), 4),
            new Customer(4, new Location(0, 4, 5), 5),
            new Customer(5, new Location(1, 8, 3), 7),
            new Customer(6, new Location(6, 8, 8), 12),
            new Customer(7, new Location(6, 4, 7), 4),
            new Customer(8, new Location(2, 3, 6), 6),
            new Customer(9, new Location(7, 7, 2), 8),
            new Customer(10, new Location(4, 1, 9), 5),
            new Customer(11, new Location(3, 6, 4), 7),
            new Customer(12, new Location(5, 2, 8), 9),
            new Customer(13, new Location(8, 3, 5), 6),
            new Customer(14, new Location(1, 7, 7), 10),
            new Customer(15, new Location(9, 9, 1), 3)
        );
    }

    public static List<Vehicle> createVehicles() {
        return Arrays.asList(
            new Vehicle(1, 10),
            new Vehicle(2, 15),
            new Vehicle(3, 20),
            new Vehicle(4, 25),
            new Vehicle(5, 18),
            new Vehicle(6, 12)
        );
    }
}
