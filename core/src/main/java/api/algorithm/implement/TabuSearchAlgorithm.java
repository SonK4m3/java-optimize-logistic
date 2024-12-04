package api.algorithm.implement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import api.algorithm.PlanningAlgorithm;
import api.solution.PlanningSolution;

public class TabuSearchAlgorithm<Solution_ extends PlanningSolution> implements PlanningAlgorithm<Solution_> {
    private static final int MAX_ITERATION = 1000000;
    private static final int MAX_ITERATIONS_WITHOUT_IMPROVEMENT = 100000;
    private static final int DEFAULT_TABU_LIST_SIZE = 10;

    private final Queue<Solution_> tabuList;
    private Solution_ bestSolution;
    private double bestScore;

    public TabuSearchAlgorithm() {
        this.tabuList = new LinkedList<>();
    }

    @Override
    public Solution_ execute(Solution_ initialSolution) {
        bestSolution = initialSolution;
        bestScore = bestSolution.calculateScore();
        Solution_ currentSolution = initialSolution;
        int iterationsWithoutImprovement = 0;

        for (int iteration = 0; iteration < MAX_ITERATION; iteration++) {
            List<Solution_> neighbors = generateNeighbors(currentSolution);
            Solution_ bestNeighbor = findBestNonTabuNeighbor(neighbors);

            if (bestNeighbor == null) {
                break; // No valid moves available
            }

            double neighborScore = bestNeighbor.calculateScore();

            // Update best solution if better score is found
            if (neighborScore < bestScore) {
                bestSolution = bestNeighbor;
                bestScore = neighborScore;
                iterationsWithoutImprovement = 0;
            } else {
                iterationsWithoutImprovement++;
            }

            // Termination criteria
            if (iterationsWithoutImprovement >= MAX_ITERATIONS_WITHOUT_IMPROVEMENT) {
                break;
            }

            // Update tabu list
            updateTabuList(bestNeighbor);
            currentSolution = bestNeighbor;
        }

        return bestSolution;
    }

    protected Solution_ findBestNonTabuNeighbor(List<Solution_> neighbors) {
        Solution_ bestNeighbor = null;
        double bestNeighborScore = Double.MAX_VALUE;

        for (Solution_ neighbor : neighbors) {
            double score = neighbor.calculateScore();

            // Apply aspiration criteria: accept if better than global best
            boolean isTabu = tabuList.contains(neighbor);
            if (!isTabu || score < bestScore) {
                if (score < bestNeighborScore) {
                    bestNeighborScore = score;
                    bestNeighbor = neighbor;
                }
            }
        }

        return bestNeighbor;
    }

    private void updateTabuList(Solution_ solution) {
        tabuList.offer(solution);
        while (tabuList.size() > DEFAULT_TABU_LIST_SIZE) {
            tabuList.poll();
        }
    }

    protected List<Solution_> generateNeighbors(Solution_ currentSolution) {
        List<Solution_> neighbors = new ArrayList<>();
        int numberOfSwaps = 10;

        for (int i = 0; i < numberOfSwaps; i++) {
            @SuppressWarnings("unchecked")
            Solution_ neighbor = (Solution_) currentSolution.clone();

            int size = neighbor.getValueRange().size();
            int pos1 = (int) (Math.random() * size);
            int pos2 = (int) (Math.random() * size);

            if (pos1 > pos2) {
                int temp = pos1;
                pos1 = pos2;
                pos2 = temp;
            }

            switch (i % 3) {
                case 0:
                    neighbor.swap(pos1, pos2);
                    break;
                case 1:
                    neighbor.insert(pos1, pos2);
                    break;
                case 2:
                    neighbor.invert(pos1, pos2);
                    break;
            }
            neighbors.add(neighbor);
        }

        return neighbors;
    }
}
