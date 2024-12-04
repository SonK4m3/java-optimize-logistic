package api.stream;

import api.solution.PlanningSolution;
import java.util.List;

/**
 * Provider interface for defining constraints in a planning problem.
 * 
 * @param <T> The type of planning solution this provider handles
 */
public interface ConstraintProvider<T extends PlanningSolution> {

    /**
     * Defines and returns the list of constraints for the planning problem.
     */
    List<Constraint<T>> defineConstraints();

    /**
     * Returns only the hard constraints that must be satisfied.
     */
    default List<Constraint<T>> getHardConstraints() {
        return defineConstraints().stream()
                .filter(c -> c.getWeight() >= 1.0)
                .toList();
    }

    /**
     * Returns only the soft constraints for optimization.
     */
    default List<Constraint<T>> getSoftConstraints() {
        return defineConstraints().stream()
                .filter(c -> c.getWeight() < 1.0)
                .toList();
    }
}
