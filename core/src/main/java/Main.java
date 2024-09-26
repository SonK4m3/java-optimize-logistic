import domain.*;
import domain.display.Display;
import domain.solver.MVRPSolver;
import domain.time.PerformanceMeter;
import domain.display.DisplayFactory;
import domain.seed.VRPSeeding;

import java.util.*;

public class Main {
    static Display display = DisplayFactory.getDisplay(DisplayFactory.DisplayType.FILE, "core/src/main/java/logs/output.log", "overwrite");
    static List<Depot> depotList = VRPSeeding.createDepots();
    static List<Customer> customerList = VRPSeeding.createCustomers();
    static List<Vehicle> vehicleList = VRPSeeding.createVehicles();

    public static void main(String[] args) {
        // Display the locations of the depots, customers, and vehicles
        display.displayLocations(depotList, customerList, vehicleList);

        // Create the initial solution
        VRPSolution initialSolution = new VRPSolution(depotList, customerList, vehicleList);
        display.displayMessage("Initial Solution:");
        display.displaySolution(initialSolution);

        // Create the MVRPSolver and use the Greed Algorithm
        MVRPSolver solver = new MVRPSolver();
        solver.usingGreedAlgorithm();
        display.displayMessage("Using Greed Algorithm");

        // Solve the problem and display the solution
        Runnable solvingTask = () -> {
            VRPSolution solution = solver.solve(initialSolution);

            display.displayMessage("\nOptimal Solution:");
            display.displaySolution(solution);
        };

        // Measure the execution time of the solving task
        try {
            PerformanceMeter.measureExecutionTime(solvingTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
