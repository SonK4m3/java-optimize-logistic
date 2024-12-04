package api.algorithm.implement;

import api.algorithm.PlanningAlgorithm;
import api.solution.PlanningSolution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import api.solution.ProblemFactCollectionProperty;

public class ACOAlgorithm<Solution_ extends PlanningSolution> implements PlanningAlgorithm<Solution_> {
    protected double[][] pheromones;
    protected double[][] distances;
    protected int maxIterations;
    protected int antCount;
    protected double alpha; // Pheromone importance factor
    protected double beta; // Distance importance factor
    protected double evaporationRate;
    protected double q; // Pheromone deposit factor
    protected Random random;
    protected Solution_ bestSolution;
    protected double bestScore;

    public ACOAlgorithm() {
        this.random = new Random();
        setParameters(1000, 100, 1.5, 2.0, 0.3, 1.5);
    }

    public void initializePheromones() {
        for (int i = 0; i < pheromones.length; i++) {
            Arrays.fill(pheromones[i], 0.1); // Initial small pheromone value
        }
    }

    public Solution_ constructSolution() {
        List<Integer> unvisitedNodes = new ArrayList<>();
        for (int i = 1; i < distances.length; i++) { // Start from 1 to skip depot
            unvisitedNodes.add(i);
        }

        List<Integer> tour = new ArrayList<>();
        int currentNode = 0; // Start from depot

        while (!unvisitedNodes.isEmpty()) {
            int nextNode = selectNextNode(currentNode, unvisitedNodes);
            if (nextNode == -1)
                break;

            int selectedNode = unvisitedNodes.get(nextNode);
            tour.add(selectedNode);
            unvisitedNodes.remove(nextNode);
            currentNode = selectedNode;
        }

        // Create and return solution based on constructed tour
        @SuppressWarnings("unchecked")
        Solution_ solution = (Solution_) bestSolution.clone();
        for (int i = 0; i < tour.size() - 1; i++) {
            solution.swap(tour.get(i), tour.get(i + 1));
        }
        return solution;
    }

    public List<Solution_> constructSolutions() {
        List<Solution_> solutions = new ArrayList<>();
        for (int i = 0; i < antCount; i++) {
            solutions.add(constructSolution());
        }
        return solutions;
    }

    public void updatePheromones(List<Solution_> solutions) {
        // Pheromone evaporation
        evaporatePheromones();

        // Add new pheromones based on solutions quality
        for (Solution_ solution : solutions) {
            double score = solution.calculateScore();
            double pheromoneDeposit = q / score; // Better solutions deposit more pheromone

            // Update pheromone on the paths used in this solution
            List<? extends ProblemFactCollectionProperty> valueRange = solution.getValueRange();
            for (int i = 0; i < valueRange.size() - 1; i++) {
                int from = i;
                int to = i + 1;
                pheromones[from][to] += pheromoneDeposit;
                pheromones[to][from] += pheromoneDeposit; // Symmetric problem
            }
        }
    }

    public void evaporatePheromones() {
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = 0; j < pheromones[i].length; j++) {
                pheromones[i][j] *= (1 - evaporationRate);
            }
        }
    }

    public double calculateProbability(int from, int to) {
        double pheromone = Math.pow(pheromones[from][to], alpha);
        double distance = Math.pow(1.0 / distances[from][to], beta);
        return pheromone * distance;
    }

    public int selectNextNode(int currentNode, List<Integer> unvisitedNodes) {
        if (unvisitedNodes.isEmpty())
            return -1;

        double[] probabilities = new double[unvisitedNodes.size()];
        double totalProbability = 0.0;

        for (int i = 0; i < unvisitedNodes.size(); i++) {
            int to = unvisitedNodes.get(i);
            probabilities[i] = calculateProbability(currentNode, to);
            totalProbability += probabilities[i];
        }

        if (totalProbability == 0)
            return -1;

        double randomValue = random.nextDouble() * totalProbability;
        double sum = 0.0;
        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            if (sum >= randomValue) {
                return i;
            }
        }

        return probabilities.length - 1;
    }

    public void setParameters(int maxIterations, int antCount, double alpha, double beta, double evaporationRate,
            double q) {
        this.maxIterations = maxIterations;
        this.antCount = antCount;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationRate = evaporationRate;
        this.q = q;
    }

    @Override
    public Solution_ execute(Solution_ initialSolution) {
        // Initialize
        this.bestSolution = initialSolution;
        this.bestScore = initialSolution.calculateScore();
        int dimension = initialSolution.getValueRange().size();
        this.pheromones = new double[dimension][dimension];
        this.distances = new double[dimension][dimension];

        // Calculate distances matrix
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i != j) {
                    @SuppressWarnings("unchecked")
                    Solution_ tempSolution = (Solution_) initialSolution.clone();
                    tempSolution.swap(i, j);
                    distances[i][j] = Math.abs(tempSolution.calculateScore() - bestScore);
                }
            }
        }

        initializePheromones();

        // Main ACO loop
        int iterationsWithoutImprovement = 0;
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            List<Solution_> solutions = constructSolutions();

            // Find best solution in current iteration
            for (Solution_ solution : solutions) {
                double score = solution.calculateScore();
                if (score < bestScore) {
                    bestScore = score;
                    bestSolution = solution;
                    iterationsWithoutImprovement = 0;
                }
            }

            updatePheromones(solutions);

            iterationsWithoutImprovement++;
            if (iterationsWithoutImprovement > maxIterations / 10) {
                break; // Early stopping if no improvement
            }
        }

        return bestSolution;
    }
}
