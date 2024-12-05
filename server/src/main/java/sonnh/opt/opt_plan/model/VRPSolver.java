package sonnh.opt.opt_plan.model;

import api.solver.Solver;

public class VRPSolver extends Solver<VRPSolution> {
    private VRPSolution mSolution;

    public VRPSolver(VRPSolution solution) {
        super();
        this.mSolution = solution;
    }

    @Override
    public VRPSolution createInitialSolution() {
        this.mSolution.initialize();
        return this.mSolution;
    }

    public VRPSolution solve() {
        return super.solve();
    }

}
