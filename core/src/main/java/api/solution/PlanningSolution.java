package api.solution;

import api.score.HardSoftScore;
import java.util.List;

public interface PlanningSolution extends Cloneable {
    @SuppressWarnings("unchecked")
    default <T extends PlanningSolution> Class<T> getSolutionClass() {
        return (Class<T>) getClass();
    }

    List<? extends ProblemFactCollectionProperty> getValueRange();

    double calculateScore();

    HardSoftScore<? extends PlanningSolution> getHardSoftScore();

    boolean canAddElement(Object element);

    void addElement(Object element);

    void swap(int pos1, int pos2);

    void insert(int from, int to);

    void invert(int start, int end);

    PlanningSolution clone();

    void initialize();
}
