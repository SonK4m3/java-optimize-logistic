package domain.solver;

import domain.*;
import domain.display.Display;
import domain.display.DisplayFactory;
import domain.geo.EuclideanDistanceCalculator;

import java.util.*;
import java.util.stream.Collectors;

import api.algorithm.implement.ACOAlgorithm;

public class VRPACOAlgorithm {
    private double[][] pheromones;
    private double[][] distances;
    private List<Customer> customers;
    private List<Vehicle> vehicles;
    private List<Depot> depots;
    private int maxIterations;
    private int antCount;
    private double alpha;
    private double beta;
    private double evaporationRate;
    private double q;
    private Random random = new Random();
    private Display display;

    public VRPACOAlgorithm() {
        this.display = DisplayFactory.getDisplay(DisplayFactory.DisplayType.FILE, "output_aco.log", "overwrite");
        setParameters(1000, 100, 1.5, 2.0, 0.3, 1.5);
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

    public VRPSolution execute(VRPSolution initialSolution) {
        this.customers = initialSolution.getCustomerList();
        this.vehicles = initialSolution.getVehicleList();
        this.depots = initialSolution.getDepotList();

        int totalNodes = customers.size() + depots.size();
        pheromones = new double[totalNodes][totalNodes];
        distances = new double[totalNodes][totalNodes];

        initializePheromones();
        calculateDistances();

        VRPSolution bestSolution = null;
        double bestScore = Double.MAX_VALUE;

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            List<VRPSolution> solutions = constructSolutions();

            for (VRPSolution solution : solutions) {
                double score = solution.calculateScore();
                if (score < bestScore) {
                    bestScore = score;
                    bestSolution = solution.clone();
                }
            }

            display.displayMessage("Iteration " + iteration + ": ");
            display.displaySolution(bestSolution);
            updatePheromones(solutions);
            evaporatePheromones();
        }

        return bestSolution;
    }

    public void initializePheromones() {
        for (int i = 0; i < pheromones.length; i++) {
            Arrays.fill(pheromones[i], 0.1);
        }
    }

    private void calculateDistances() {
        List<Location> allLocations = new ArrayList<>();
        allLocations.addAll(customers.stream().map(Customer::getLocation).collect(Collectors.toList()));
        allLocations.addAll(depots.stream().map(Depot::getLocation).collect(Collectors.toList()));

        EuclideanDistanceCalculator calculator = new EuclideanDistanceCalculator();

        for (int i = 0; i < allLocations.size(); i++) {
            for (int j = 0; j < allLocations.size(); j++) {
                distances[i][j] = calculator.calculateDistance(allLocations.get(i), allLocations.get(j));
            }
        }
    }

    public List<VRPSolution> constructSolutions() {
        List<VRPSolution> solutions = new ArrayList<>();
        for (int i = 0; i < antCount; i++) {
            solutions.add(constructSolution());
        }
        return solutions;
    }

    public VRPSolution constructSolution() {
        VRPSolution solution = new VRPSolution(depots, new ArrayList<>(customers), new ArrayList<>(vehicles));
        List<Customer> unvisitedCustomers = new ArrayList<>(customers);

        for (Vehicle vehicle : solution.getVehicleList()) {
            if (unvisitedCustomers.isEmpty())
                break;

            Depot depot = vehicle.getDepot();
            int currentNode = depots.indexOf(depot);
            int remainingCapacity = vehicle.getCapacity();

            while (!unvisitedCustomers.isEmpty() && remainingCapacity > 0) {
                int nextNode = selectNextNode(currentNode, unvisitedCustomers.stream()
                        .map(customers::indexOf)
                        .collect(Collectors.toList()));
                if (nextNode == -1)
                    break;

                Customer nextCustomer = unvisitedCustomers.get(nextNode);
                if (remainingCapacity >= nextCustomer.getDemand()) {
                    vehicle.addCustomer(nextCustomer);
                    remainingCapacity -= nextCustomer.getDemand();
                    unvisitedCustomers.remove(nextNode);
                    currentNode = customers.indexOf(nextCustomer);
                } else {
                    break;
                }
            }
        }

        return solution;
    }

    public void updatePheromones(List<VRPSolution> solutions) {
        for (VRPSolution solution : solutions) {
            double score = solution.calculateScore();
            for (Vehicle vehicle : solution.getVehicleList()) {
                List<Customer> route = vehicle.getCustomerList();
                if (route.isEmpty())
                    continue;

                int from = depots.indexOf(vehicle.getDepot()) + customers.size();
                for (Customer customer : route) {
                    int to = customers.indexOf(customer);
                    pheromones[from][to] += q / score;
                    from = to;
                }
                pheromones[from][depots.indexOf(vehicle.getDepot()) + customers.size()] += q / score;
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
}
