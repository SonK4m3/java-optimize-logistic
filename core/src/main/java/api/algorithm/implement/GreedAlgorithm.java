package api.algorithm.implement;

import api.algorithm.PlanningAlgorithm;
import api.solution.PlanningSolution;
import api.solution.ProblemFactCollectionProperty;
import java.util.*;

public class GreedAlgorithm<Solution_ extends PlanningSolution> implements PlanningAlgorithm<Solution_> {
    private Solution_ bestSolution;
    private double bestScore;

    @Override
    public Solution_ execute(Solution_ initialSolution) {
        bestSolution = initialSolution;
        bestScore = initialSolution.calculateScore();

        List<? extends ProblemFactCollectionProperty> valueRange = initialSolution.getValueRange();

        for (ProblemFactCollectionProperty element : valueRange) {
            if (bestSolution.canAddElement(element)) {
                @SuppressWarnings("unchecked")
                Solution_ candidate = (Solution_) bestSolution.clone();
                candidate.addElement(element);

                double candidateScore = candidate.calculateScore();
                if (candidateScore < bestScore) {
                    bestSolution = candidate;
                    bestScore = candidateScore;
                }
            }
        }

        return bestSolution;
    }
}
