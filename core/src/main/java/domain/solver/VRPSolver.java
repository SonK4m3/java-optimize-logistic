package domain.solver;

import api.solver.Solver;
import domain.VRPSolution;

public class VRPSolver {
    private Solver<VRPSolution> solver;
    private VRPSolution currentSolution;
    private VRPSolution optimalSolution;

    private static final int TABU_LIST_SIZE = 10000;

    public VRPSolver() {
        this.solver = new Solver<>();
    }

    public void setAlgorithm(EVRPAlgorithm algorithm) {
        switch (algorithm) {
            case GREED:
                this.solver.setAlgorithm(new VRPGreedAlgorithm());
                break;
            case TABU:
                this.solver.setAlgorithm(new VRPTabuSearchAlgorithm(TABU_LIST_SIZE));
                break;
            case ACO:
                this.solver.setAlgorithm(new VRPACOAlgorithm());
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm");
        }
    }

    public VRPSolution solve(VRPSolution newSolution) {
        this.currentSolution = newSolution;
        this.optimalSolution = this.solver.solve(newSolution);
        return this.optimalSolution;
    }

    public VRPSolution getOptimalSolution() {
        return this.optimalSolution;
    }

    public VRPSolution getCurrentSolution() {
        return this.currentSolution;
    }
}
