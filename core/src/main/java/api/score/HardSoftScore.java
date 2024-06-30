package api.score;

import api.solution.PlanningSolution;
import api.stream.Constraint;

import java.util.List;

public class HardSoftScore {
    public final static double HARD_SCORE_INFINITY = Double.POSITIVE_INFINITY;
    private final PlanningSolution solution;
    private final List<Constraint> hardSoftConstraints;

    public HardSoftScore(PlanningSolution solution, List<Constraint> hardSoftConstraints) {
        this.solution = solution;
        this.hardSoftConstraints = hardSoftConstraints;
    }

    public double calculateScore() {
        // Calculate soft score
        double softScore = 0;
        for (Constraint constraint : this.hardSoftConstraints) {
            if (!constraint.isSatisfied(solution)) {
                return HARD_SCORE_INFINITY;
            }
            softScore += constraint.getScore(solution);
        }

        return softScore;
    }
}
