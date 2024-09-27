package api.algorithm;

import api.solution.PlanningSolution;
import java.util.List;

public interface ACOAlgorithm<Solution_ extends PlanningSolution> extends PlanningAlgorithm<Solution_> {
    void initializePheromones();
    Solution_ constructSolution();
    List<Solution_> constructSolutions();
    void updatePheromones(List<Solution_> solutions);
    void evaporatePheromones();
    double calculateProbability(int from, int to);
    int selectNextNode(int currentNode, List<Integer> unvisitedNodes);
    void setParameters(int maxIterations, int antCount, double alpha, double beta, double evaporationRate, double q);
    @Override
    Solution_ solve(Solution_ initialSolution);
}
