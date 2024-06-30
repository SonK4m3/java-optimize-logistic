package domain.solver;

import api.solution.PlanningSolution;

import java.util.List;

public interface TabuSearch<T extends PlanningSolution> {
    T initialSolution(T rawSolution);
    List<T> generateNeighbors(T currentSolution);
}
