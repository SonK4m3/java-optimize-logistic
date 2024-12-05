package api.algorithm;

import api.solution.PlanningSolution;

public interface PlanningAlgorithm<PlanningSolution> {
    PlanningSolution execute(PlanningSolution newSolution);
}
