package api.solution;

import java.util.List;

public interface PlanningSolution {
    List<? extends ProblemFactCollectionProperty> getValueRange();
    double calculateScore();
}
