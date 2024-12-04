package api.algorithm;

import api.solution.PlanningSolution;

public interface PlanningAlgorithm<S extends PlanningSolution> {
    S execute(S newSolution);
}
