package api.stream;

import api.solution.PlanningSolution;

/**
 * Represents a constraint in a planning problem.
 * 
 * Constraints can be either hard or soft:
 * - Hard constraints must be satisfied for a solution to be valid.
 * - Soft constraints contribute to the optimality of a solution.
 */
public interface Constraint {

    /**
     * Checks if the constraint is satisfied by the given solution.
     *
     * @param solution The planning solution to evaluate.
     * @return true if the constraint is satisfied, false otherwise.
     */
    boolean isSatisfied(PlanningSolution solution);

    /**
     * Calculates the score of the constraint for the given solution.
     * 
     * For hard constraints, this typically returns 0 if satisfied, and a large positive value if violated.
     * For soft constraints, this returns a value representing the degree of satisfaction or violation.
     *
     * @param solution The planning solution to evaluate.
     * @return The score of the constraint. Lower scores are generally better.
     */
    double getScore(PlanningSolution solution);

    /**
     * Returns the weight of this constraint.
     * 
     * This allows for more flexible constraint definitions and easier balancing of multiple constraints.
     *
     * @return The weight of the constraint.
     */
    default double getWeight() {
        return 1.0;
    }
}
