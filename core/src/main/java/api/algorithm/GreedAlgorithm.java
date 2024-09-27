package api.algorithm;

import api.solution.PlanningSolution;
import api.solution.ProblemFactCollectionProperty;

public interface GreedAlgorithm<Solution_ extends PlanningSolution> extends PlanningAlgorithm<Solution_> {
    ProblemFactCollectionProperty greedRangeValue();
}
