package api.stream;

import api.solution.PlanningSolution;

public interface Constraint {
    boolean isSatisfied(PlanningSolution solution);

    double getScore(PlanningSolution solution);
}
