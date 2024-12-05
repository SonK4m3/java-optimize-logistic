package api.solver;

import api.algorithm.PlanningAlgorithm;
import api.algorithm.implement.TabuSearchAlgorithm;
import api.solution.PlanningSolution;
import api.algorithm.AlgorithmFactory;

public abstract class Solver<S extends PlanningSolution> {
    private static final String DEFAULT_ALGORITHM = "TABU";
    public PlanningAlgorithm<S> algorithm;
    public S initialSolution;
    public S currentSolution;
    public S optimalSolution;

    public Solver() {
        this.initialSolution = this.createInitialSolution();

        this.algorithm = new TabuSearchAlgorithm<S>();
        System.out.println("Algorithm: " + this.algorithm);
    }

    public S solve() {
        validateInputs(initialSolution);

        System.out.println("Algorithm: " + this.algorithm);

        this.currentSolution = initialSolution;
        this.optimalSolution = algorithm.execute(initialSolution);

        return this.optimalSolution;
    }

    // public void useAlgorithm(String algorithm) {
    //     PlanningAlgorithm<PlanningSolution> planningAlgorithm = AlgorithmFactory.getAlgorithm(algorithm);
    //     this.algorithm = planningAlgorithm;
    // }

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
