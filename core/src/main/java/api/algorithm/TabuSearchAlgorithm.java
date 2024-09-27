package api.algorithm;

import java.util.List;

import api.solution.PlanningSolution;

public interface TabuSearchAlgorithm<Solution_ extends PlanningSolution> extends PlanningAlgorithm<Solution_> {
    Solution_ initialSolution(Solution_ solution);
    /**
     * Generates neighboring solutions by applying various operators
     * Includes intra-route swaps, inter-route swaps, and relocations
     */
    List<Solution_> generateNeighbors(Solution_ currentSolution);
}
