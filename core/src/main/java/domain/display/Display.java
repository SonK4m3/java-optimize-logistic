package domain.display;

import domain.*;

import java.util.List;

public interface Display {
    void displayCustomers(List<Customer> customers);

    void displayVehicles(List<Vehicle> vehicles);

    void displaySolution(VRPSolution solution);

    void displayLocations(List<Depot> depots, List<Customer> customers, List<Vehicle> vehicles);
}
