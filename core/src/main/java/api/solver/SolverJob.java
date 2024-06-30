package api.solver;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

public interface SolverJob<Solution_> {
    SolverStatus getStatus();
    void terminateEarly();
    Solution_ getFinalBestSolution() throws InterruptedException, ExecutionException;
    Duration getSolvingDuration();
}
