package domain.display;

import domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsoleDisplay implements Display {
    private static final int GRID_SIZE = 15; // Adjust for larger maps

    @Override
    public void displayCustomers(List<Customer> customers) {
        customers.forEach(this::printCustomer);
    }

    @Override
    public void displayVehicles(List<Vehicle> vehicles) {
        vehicles.forEach(this::printVehicle);
    }

    @Override
    public void displaySolution(VRPSolution solution) {
        solution.getVehicleList().forEach(vehicle -> {
            System.out.print("Vehicle " + vehicle.getId() + " : D" + vehicle.getDepot().getId() + " ");
            vehicle.getCustomerList().forEach(customer -> {
                        System.out.print(customer.getId() + " ");
                    }
            );
            System.out.println();
        });
        System.out.println("Total: " + String.format("%.2f", solution.calculateScore()));
    }

    private void printCustomer(Customer customer) {
        System.out.println("Customer ID: " + customer.getId());
        System.out.println("Location: (" + customer.getLocation().getX() + ", " + customer.getLocation().getY() + ")");
        System.out.println("Demand: " + customer.getDemand());
        System.out.println("--------------------");
    }

    private void printVehicle(Vehicle vehicle) {
        System.out.println("Vehicle ID: " + vehicle.getId());
        System.out.println("Capacity: " + vehicle.getCapacity());
        System.out.println("--------------------");
    }

    @Override
    public void displayLocations(List<Depot> depots, List<Customer> customers, List<Vehicle> vehicles) {
        Map<Location, String> locationSymbols = new HashMap<>();
        depots.forEach(depot -> {
            locationSymbols.put(depot.getLocation(), "D");
        });

        int customerSymbol = 1;
        for (Customer customer : customers) {
            locationSymbols.put(customer.getLocation(), String.valueOf(customerSymbol));
            customerSymbol++;
        }

        for (int y = GRID_SIZE - 1; y >= 0; y--) {
            for (int x = 0; x < GRID_SIZE; x++) {
                String symbol = "."; // Empty space
                for (Map.Entry<Location, String> entry : locationSymbols.entrySet()) {
                    Location location = entry.getKey();
                    if ((int) location.getX() == x && (int) location.getY() == y) {
                        symbol = entry.getValue();
                        break;
                    }
                }
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }
}
