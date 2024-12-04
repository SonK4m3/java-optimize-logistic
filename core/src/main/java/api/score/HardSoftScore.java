package api.score;

import api.solution.PlanningSolution;
import api.stream.Constraint;

import java.util.List;

public class HardSoftScore<T extends PlanningSolution> {
    public final static double HARD_SCORE_INFINITY = Double.POSITIVE_INFINITY;
    private final T solution;
    private final List<Constraint<T>> hardSoftConstraints;

    public HardSoftScore(T solution, List<Constraint<T>> hardSoftConstraints) {
        this.solution = solution;
        this.hardSoftConstraints = hardSoftConstraints;
    }

    /**
     * Calculates the score of the solution based on hard and soft constraints.
     * If any hard constraint is not satisfied, returns HARD_SCORE_INFINITY.
     * Otherwise, returns the sum of the scores of all satisfied constraints.
     *
     * @return the calculated score
     */
    public double calculateScore() {
        double totalScore = 0;
        for (Constraint<T> constraint : this.hardSoftConstraints) {
            if (!constraint.isSatisfied(solution)) {
                return HARD_SCORE_INFINITY;
            }
            totalScore += constraint.getScore(solution);
        }
        return totalScore;
    }
}
