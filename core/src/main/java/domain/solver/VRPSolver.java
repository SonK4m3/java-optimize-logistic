package domain.solver;

import api.solution.PlanningSolution;
import domain.Customer;
import domain.Depot;
import domain.VRPSolution;
import domain.Vehicle;
import domain.display.Display;
import domain.display.DisplayFactory;

import java.util.*;
import java.util.stream.Collectors;

public class VRPSolver implements TabuSearch<VRPSolution> {
    private static final int MAX_ITERATION = 1000;
    private static final int MAX_ITERATIONS_WITHOUT_IMPROVEMENT = 1000;
    private final int tabuListSize;
    private final Queue<PlanningSolution> tabuList;
    private final Display display;

    public VRPSolver(int tabuListSize) {
        this.tabuListSize = tabuListSize;
        this.tabuList = new LinkedList<>();
        this.display = DisplayFactory.getDisplay(DisplayFactory.DisplayType.FILE, "core/src/main/java/logs/output.log");
    }

    /**
     * Solves the VRP using Tabu Search algorithm
     * Iteratively improves the solution until a stopping criterion is met
     */
    public VRPSolution solve(VRPSolution initialSolution) {
        VRPSolution currentSolution = initialSolution.clone();
        VRPSolution bestSolution = initialSolution.clone();
        tabuList.add(initialSolution);

        int iteration = 0;
        int iterationsWithoutImprovement = 0;
        while (iteration < MAX_ITERATION && iterationsWithoutImprovement < MAX_ITERATIONS_WITHOUT_IMPROVEMENT) {
            List<VRPSolution> neighbors = generateNeighbors(currentSolution);
            VRPSolution bestNeighbor = findBestNeighbor(neighbors);

            if (bestNeighbor == null) {
                break;
            }

            currentSolution = bestNeighbor.clone();
            if (currentSolution.calculateScore() < bestSolution.calculateScore()) {
                bestSolution = currentSolution.clone();
                iterationsWithoutImprovement = 0;
                this.display.displayMessage("--- step: " + iteration);
                this.display.displaySolution(bestSolution);
            } else {
                iterationsWithoutImprovement++;
            }

            updateTabuList(currentSolution);
            iteration++;
        }

        return bestSolution;
    }

    /**
     * Finds the best non-tabu neighbor from a list of neighbors
     */
    private VRPSolution findBestNeighbor(List<VRPSolution> neighbors) {
        return neighbors.stream()
                .filter(solution -> !tabuList.contains(solution))
                .min(Comparator.comparingDouble(VRPSolution::calculateScore))
                .orElse(null);
    }

    /**
     * Updates the tabu list with a new solution
     * Removes the oldest solution if the list size exceeds the limit
     */
    private void updateTabuList(VRPSolution solution) {
        tabuList.add(solution);
        if (tabuList.size() > tabuListSize) {
            tabuList.remove();
        }
    }

    /**
     * Generates an initial solution for the VRP
     * Assigns customers to depots and vehicles, ensuring capacity constraints
     */
    @Override
    public VRPSolution initialSolution(VRPSolution rawSolution) {
        Map<Depot, List<Customer>> depotCustomers = assignCustomersToDepots(rawSolution.getDepotList(), rawSolution.getCustomerList());
        rawSolution.getVehicleList().sort(Comparator.comparingInt(Vehicle::getCapacity).reversed());
        assignVehiclesToDepots(rawSolution.getVehicleList(), rawSolution.getDepotList(), depotCustomers);

        Set<Customer> assignedCustomers = new HashSet<>();
        for (Vehicle vehicle : rawSolution.getVehicleList()) {
            List<Customer> customers = depotCustomers.get(vehicle.getDepot());
            if (customers != null && !customers.isEmpty()) {
                customers.sort(Comparator.comparingDouble(customer -> 
                    vehicle.getDepot().getLocation().getDistanceTo(customer.getLocation())));

                for (Customer customer : customers) {
                    if (!assignedCustomers.contains(customer) && vehicle.canAddCustomer(customer)) {
                        vehicle.addCustomer(customer);
                        assignedCustomers.add(customer);
                    }
                }
            }
        }

        // Ensure that each vehicle has at least one customer
        for (Vehicle vehicle : rawSolution.getVehicleList()) {
            if (vehicle.getCustomerList().isEmpty()) {
                for (Customer customer : rawSolution.getCustomerList()) {
                    if (!assignedCustomers.contains(customer) && vehicle.canAddCustomer(customer)) {
                        vehicle.addCustomer(customer);
                        assignedCustomers.add(customer);
                        break;
                    }
                }
            }
        }

        return rawSolution;
    }

    /**
     * Generates neighboring solutions by applying various operators
     * Includes intra-route swaps, inter-route swaps, and relocations
     */
    @Override
    public List<VRPSolution> generateNeighbors(VRPSolution currentSolution) {
        List<VRPSolution> neighbors = new ArrayList<>();
        neighbors.addAll(generateIntraRouteSwaps(currentSolution));
        neighbors.addAll(generateInterRouteSwaps(currentSolution));
        neighbors.addAll(generateRelocations(currentSolution));
        return neighbors;
    }

    /**
     * Generates neighboring solutions by swapping customers within the same route
     */
    private List<VRPSolution> generateIntraRouteSwaps(VRPSolution solution) {
        List<VRPSolution> neighbors = new ArrayList<>();
        for (Vehicle vehicle : solution.getVehicleList()) {
            if (vehicle.getCustomerList().size() < 2) continue;
            for (int i = 0; i < vehicle.getCustomerList().size(); i++) {
                for (int j = i + 1; j < vehicle.getCustomerList().size(); j++) {
                    VRPSolution neighbor = solution.clone();
                    Vehicle clonedVehicle = getVehicleById(neighbor.getVehicleList(), vehicle.getId());
                    Collections.swap(clonedVehicle.getCustomerList(), i, j);
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    /**
     * Generates neighboring solutions by swapping customers between different routes
     */
    private List<VRPSolution> generateInterRouteSwaps(VRPSolution solution) {
        List<VRPSolution> neighbors = new ArrayList<>();
        List<Vehicle> vehicles = solution.getVehicleList();
        for (int i = 0; i < vehicles.size(); i++) {
            for (int j = i + 1; j < vehicles.size(); j++) {
                Vehicle v1 = vehicles.get(i);
                Vehicle v2 = vehicles.get(j);
                if (v1.getCustomerList().isEmpty() || v2.getCustomerList().isEmpty()) continue;
                for (int k = 0; k < v1.getCustomerList().size(); k++) {
                    for (int l = 0; l < v2.getCustomerList().size(); l++) {
                        VRPSolution neighbor = solution.clone();
                        Vehicle clonedV1 = getVehicleById(neighbor.getVehicleList(), v1.getId());
                        Vehicle clonedV2 = getVehicleById(neighbor.getVehicleList(), v2.getId());
                        if (swapCustomersBetweenVehicles(clonedV1, clonedV2, k, l)) {
                            neighbors.add(neighbor);
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    /**
     * Generates neighboring solutions by relocating customers between routes
     */
    private List<VRPSolution> generateRelocations(VRPSolution solution) {
        List<VRPSolution> neighbors = new ArrayList<>();
        for (Vehicle fromVehicle : solution.getVehicleList()) {
            if (fromVehicle.getCustomerList().isEmpty()) continue;
            for (int i = 0; i < fromVehicle.getCustomerList().size(); i++) {
                for (Vehicle toVehicle : solution.getVehicleList()) {
                    if (fromVehicle != toVehicle) {
                        for (int j = 0; j <= toVehicle.getCustomerList().size(); j++) {
                            VRPSolution neighbor = solution.clone();
                            Vehicle clonedFromVehicle = getVehicleById(neighbor.getVehicleList(), fromVehicle.getId());
                            Vehicle clonedToVehicle = getVehicleById(neighbor.getVehicleList(), toVehicle.getId());
                            if (relocateCustomer(clonedFromVehicle, clonedToVehicle, i, j)) {
                                neighbors.add(neighbor);
                            }
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    /**
     * Swaps customers between two vehicles if capacity constraints are met
     */
    private boolean swapCustomersBetweenVehicles(Vehicle v1, Vehicle v2, int index1, int index2) {
        Customer c1 = v1.getCustomerList().get(index1);
        Customer c2 = v2.getCustomerList().get(index2);
        
        if (v1.canAddCustomer(c2) && v2.canAddCustomer(c1)) {
            v1.getCustomerList().set(index1, c2);
            v2.getCustomerList().set(index2, c1);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a vehicle by its ID from a list of vehicles
     */
    private Vehicle getVehicleById(List<Vehicle> vehicles, long id) {
        return vehicles.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }

    /**
     * Relocates a customer from one vehicle to another if capacity constraints are met
     */
    private boolean relocateCustomer(Vehicle fromVehicle, Vehicle toVehicle, int fromIndex, int toIndex) {
        if (fromVehicle.getCustomerList().isEmpty()) {
            return false;
        }
        Customer customer = fromVehicle.getCustomerList().get(fromIndex);
        if (toVehicle.canAddCustomer(customer)) {
            fromVehicle.getCustomerList().remove(fromIndex);
            toVehicle.getCustomerList().add(toIndex, customer);
            return true;
        }
        return false;
    }

    /**
     * Assigns customers to their nearest depot
     */
    private Map<Depot, List<Customer>> assignCustomersToDepots(List<Depot> depots, List<Customer> customers) {
        Map<Depot, List<Customer>> result = depots.stream()
                .collect(Collectors.toMap(depot -> depot, depot -> new ArrayList<>()));
        
        for (Customer customer : customers) {
            Depot nearestDepot = findNearestDepot(customer, depots);
            result.get(nearestDepot).add(customer);
        }
        
        return result;
    }

    /**
     * Finds the nearest depot for a given customer
     */
    private Depot findNearestDepot(Customer customer, List<Depot> depots) {
        return depots.stream()
                .min(Comparator.comparingDouble(depot -> 
                    customer.getLocation().getDistanceTo(depot.getLocation())))
                .orElseThrow(() -> new RuntimeException("No depot found"));
    }

    /**
     * Assigns vehicles to depots based on capacity and demand
     */
    private void assignVehiclesToDepots(List<Vehicle> vehicles, List<Depot> depots, Map<Depot, List<Customer>> depotCustomers) {
        Map<Depot, Integer> depotTotalDemand = calculateDepotTotalDemand(depotCustomers);
        
        for (Vehicle vehicle : vehicles) {
            Depot bestDepot = depots.stream()
                    .min(Comparator.comparingInt(depot -> 
                        Math.abs(vehicle.getCapacity() - depotTotalDemand.getOrDefault(depot, 0))))
                    .orElse(depots.get(0));
        
            vehicle.setDepot(bestDepot);
            depotTotalDemand.put(bestDepot, depotTotalDemand.getOrDefault(bestDepot, 0) - vehicle.getCapacity());
        }
    }

    /**
     * Calculates the total demand for each depot based on assigned customers
     */
    private Map<Depot, Integer> calculateDepotTotalDemand(Map<Depot, List<Customer>> depotCustomers) {
        return depotCustomers.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().stream().mapToInt(Customer::getDemand).sum()
                ));
    }
}
