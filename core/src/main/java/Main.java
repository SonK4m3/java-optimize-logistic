import domain.*;
import domain.display.Display;
import domain.solver.VRPSolver;
import domain.time.PerformanceMeter;
import domain.display.DisplayFactory;
import domain.seed.VRPSeeding;

import java.util.*;

public class Main {
    static Display display = DisplayFactory.getDisplay(DisplayFactory.DisplayType.FILE, "core/src/main/java/logs/output.log", "overwrite");
    static List<Depot> depotList = VRPSeeding.createDepots();
    static List<Customer> customerList = VRPSeeding.createCustomers();
    static List<Vehicle> vehicleList = VRPSeeding.createVehicles();

    static final int TABU_LIST_SIZE = 1000;

    public static void main(String[] args) {
        // Display the locations of the depots, customers, and vehicles
        display.displayLocations(depotList, customerList, vehicleList);

        // Create the MVRPSolver and use the Greed Algorithm
        VRPSolver solver = new VRPSolver(TABU_LIST_SIZE);
        display.displayMessage("Using Tabu Search Algorithm");
        
        // Solve the problem and display the solution
        Runnable solvingTask = () -> {
            VRPSolution initialSolution = solver.initialSolution(new VRPSolution(depotList, customerList, vehicleList));
            display.displayMessage("Initial Solution:");
            display.displaySolution(initialSolution);

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
