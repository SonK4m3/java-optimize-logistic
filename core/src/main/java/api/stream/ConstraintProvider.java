package api.stream;

import java.util.List;

public interface ConstraintProvider {
    List<Constraint> defineConstraints(Object problem);
}
