import domain.*;
import domain.display.Display;
import domain.solver.EVRPAlgorithm;
import domain.solver.VRPSolver;
import domain.time.PerformanceMeter;
import domain.display.DisplayFactory;
import domain.seed.VRPSeeding;

import java.util.*;

public class Main {
    static Display display = DisplayFactory.getDisplay(DisplayFactory.DisplayType.FILE,
            "output_tabu.log", "overwrite");
    static Display scoreDisplay = DisplayFactory.getDisplay(DisplayFactory.DisplayType.FILE,
            "output.log");
    static List<Depot> depotList = VRPSeeding.createDepots();
    static List<Customer> customerList = VRPSeeding.createCustomers();
    static List<Vehicle> vehicleList = VRPSeeding.createVehicles();

    public static void main(String[] args) {
        // Display the locations of the depots, customers, and vehicles
        display.displayLocations(depotList, customerList, vehicleList);

        // Create the MVRPSolver
        VRPSolver solver = new VRPSolver();
        solver.setAlgorithm(EVRPAlgorithm.TABU);
        scoreDisplay.displayMessage("\nUsing Tabu Search Algorithm");

        // Measure the execution time of the solving task
        try {
            // Create the new solution to solve
            VRPSolution newSolution = new VRPSolution(depotList, customerList, vehicleList);
            display.displayMessage("New Solution:");
            display.displaySolution(newSolution);
            
            PerformanceMeter.startMeasurement();
            VRPSolution optimalSolution = solver.solve(newSolution);
            PerformanceMeter.endMeasurement();

            display.displayMessage("\nOptimal Solution:");
            display.displaySolution(optimalSolution);
            
            double executionTime = PerformanceMeter.getLastExecutionTimeSeconds();
            scoreDisplay.displayMessage("Score: " + (optimalSolution != null ? optimalSolution.calculateScore() : "null") + "\nExecution time: " + String.format("%.6f", executionTime) + " seconds");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
