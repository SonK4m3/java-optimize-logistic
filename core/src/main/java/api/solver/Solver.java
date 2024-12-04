package api.solver;

import api.algorithm.PlanningAlgorithm;
import api.solution.PlanningSolution;
import domain.solver.EVRPAlgorithm;
import api.algorithm.AlgorithmFactory;

public abstract class Solver<S extends PlanningSolution> {
    private static final EVRPAlgorithm DEFAULT_ALGORITHM = EVRPAlgorithm.TABU;
    protected PlanningAlgorithm<S> algorithm;
    protected S initialSolution;
    protected S currentSolution;
    protected S optimalSolution;

    protected Solver() {
        this.algorithm = null;
        this.initialSolution = this.createInitialSolution();

        useAlgorithm(DEFAULT_ALGORITHM);
    }

    protected Solver(PlanningAlgorithm<S> algorithm) {
        this.algorithm = algorithm;
        this.initialSolution = this.createInitialSolution();
    }

    public S solve(S initialSolution) {
        validateInputs(initialSolution);

        this.currentSolution = initialSolution;
        this.optimalSolution = algorithm.execute(initialSolution);

        return this.optimalSolution;
    }

    public void useAlgorithm(EVRPAlgorithm algorithm) {
        PlanningAlgorithm<S> planningAlgorithm = AlgorithmFactory.getAlgorithm(algorithm);
        this.algorithm = planningAlgorithm;
    }

    public S getCurrentSolution() {
        return this.currentSolution;
    }

    public S getOptimalSolution() {
        return this.optimalSolution;
    }

    protected void validateInputs(S solution) {
        if (solution == null) {
            throw new IllegalArgumentException("Solution cannot be null");
        }

        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm must be set before solving");
        }
    }

    abstract public S createInitialSolution();
}
