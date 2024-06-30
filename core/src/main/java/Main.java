import domain.*;
import domain.display.ConsoleDisplay;
import domain.display.Display;
import domain.geo.EuclideanDistanceCalculator;
import domain.solver.VRPSolver;
import domain.time.PerformanceMeter;

import java.util.*;
import java.util.concurrent.Callable;

public class Main {
    static Display display = new ConsoleDisplay();

    public static void main(String[] args) {
        List<Depot> depotList = List.of(
                new Depot(1, new Location(0, 0, 5)),
                new Depot(2, new Location(1, 8, 7))
        );

        List<Customer> customerList = Arrays.asList(
                new Customer(1, new Location(0, 2, 3), 3),
                new Customer(2, new Location(0, 5, 1), 4),
                new Customer(3, new Location(0, 1, 4), 4),
                new Customer(4, new Location(0, 4, 5), 5),
                new Customer(5, new Location(1, 8, 3), 7),
                new Customer(6, new Location(6, 8, 8), 12),
                new Customer(7, new Location(6, 4, 7), 4)
        );

        List<Vehicle> vehicleList = Arrays.asList(
                new Vehicle(1, 10),
                new Vehicle(2, 15),
                new Vehicle(3, 20)
        );

//        display.displayCustomers(customers);
        display.displayVehicles(vehicleList);
        display.displayLocations(depotList, customerList, vehicleList);

        VRPSolver solver = new VRPSolver(1000);
        Runnable solveTask = () -> {
            VRPSolution initialSolution = solver.initialSolution(new VRPSolution(depotList, customerList, vehicleList));

            System.out.println("\nInitial Solution:");
            display.displaySolution(initialSolution);

            VRPSolution solution = solver.solve(initialSolution);

            System.out.println("\nOptimal Solution:");
//            solution.getVehicleList().stream().sorted(Comparator.comparingInt(Vehicle::getRemainingCapacity));
            display.displaySolution(solution);
        };

        try {
            PerformanceMeter.measureExecutionTime(solveTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
