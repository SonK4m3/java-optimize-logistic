package api.solver;

import api.algorithm.PlanningAlgorithm;
import api.solution.PlanningSolution;

public class Solver<Solution_ extends PlanningSolution> {
    private PlanningAlgorithm<Solution_> algorithm;

    public Solver() {
        this.algorithm = null;
    }

    public Solver(PlanningAlgorithm<Solution_> algorithm) {
        this.algorithm = algorithm;
    }

    public Solution_ solve(Solution_ newSolution) {
        if(newSolution == null) {
            throw new IllegalArgumentException("New solution cannot be null");
        }

        if(algorithm == null) {
            throw new IllegalArgumentException("Algorithm cannot be null");
        }

        return algorithm.solve(newSolution);
    }

    public void setAlgorithm(PlanningAlgorithm<Solution_> algorithm) {
        this.algorithm = algorithm;
    }
}
