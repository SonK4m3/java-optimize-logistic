package api.solution;

import api.score.HardSoftScore;
import java.util.List;
import java.util.Map;

public abstract class PlanningSolution implements Cloneable {
    protected Map<String, Object> problemConstraints;
    protected Map<String, Object> solutionProperties;

    @SuppressWarnings("unchecked")
    public <T extends PlanningSolution> Class<T> getSolutionClass() {
        return (Class<T>) getClass();
    }

    public abstract List<? extends ProblemFactCollectionProperty> getValueRange();

    public abstract double calculateScore();

    protected abstract HardSoftScore<? extends PlanningSolution> getHardSoftScore();

    public abstract boolean canAddElement(Object element);

    public abstract void addElement(Object element);

    public abstract void swap(int pos1, int pos2);

    public abstract void insert(int from, int to);

    public abstract void invert(int start, int end);

    @Override
    public abstract PlanningSolution clone();

    public abstract void initialize();
}
