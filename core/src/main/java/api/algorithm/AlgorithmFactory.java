package api.algorithm;

import api.solution.PlanningSolution;
import domain.solver.EVRPAlgorithm;

import api.algorithm.implement.ACOAlgorithm;
import api.algorithm.implement.GreedAlgorithm;
import api.algorithm.implement.TabuSearchAlgorithm;

public class AlgorithmFactory {
    public static <Solution_ extends PlanningSolution> PlanningAlgorithm<Solution_> getAlgorithm(
            EVRPAlgorithm algorithm) {
        return switch (algorithm) {
            case TABU -> new TabuSearchAlgorithm<>();
            case GREED -> new GreedAlgorithm<>();
            case ACO -> new ACOAlgorithm<>();
            default -> null;
        };
    }
}
