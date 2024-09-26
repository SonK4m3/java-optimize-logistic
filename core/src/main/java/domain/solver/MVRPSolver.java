package domain.solver;

import api.solver.Solver;
import domain.VRPSolution;

public class MVRPSolver {
    private Solver<VRPSolution> solver;
    private VRPSolution currentSolution;
    private VRPSolution optimalSolution;

    public MVRPSolver() {
        this.solver = new Solver<>();
    }

    public void usingGreedAlgorithm() {
        this.solver.setAlgorithm(new GreedAlgorithm());
    }

    public void usingTabuSearchAlgorithm() {
        // this.solver.setAlgorithm(new TabuSearchAlgorithm());
    }

    public VRPSolution solve(VRPSolution initialSolution) {
        this.currentSolution = initialSolution;
        this.optimalSolution = this.solver.solve(initialSolution);
        return this.optimalSolution;
    }

    public VRPSolution getOptimalSolution() {
        return this.optimalSolution;
    }

    public VRPSolution getCurrentSolution() {
        return this.currentSolution;
    }
}
