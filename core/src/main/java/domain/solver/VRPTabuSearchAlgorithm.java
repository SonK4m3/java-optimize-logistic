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

public class VRPTabuSearchAlgorithm {
    private static final int MAX_ITERATION = 1000000;
    private static final int MAX_ITERATIONS_WITHOUT_IMPROVEMENT = 100000;
    private final int tabuListSize;
    private final Queue<PlanningSolution> tabuList;
    private final Display display;

    public VRPTabuSearchAlgorithm(int tabuListSize) {
        this.tabuListSize = tabuListSize;
        this.tabuList = new LinkedList<>();
        this.display = DisplayFactory.getDisplay(DisplayFactory.DisplayType.FILE, "core/src/main/java/logs/output.log");
    }

    public VRPSolution execute(VRPSolution newSolution) {
        VRPSolution initialSolution = this.initialSolution(newSolution);
        VRPSolution currentSolution = initialSolution.clone();
        VRPSolution bestSolution = initialSolution.clone();
        tabuList.add(initialSolution);

        int iteration = 0;
        int iterationsWithoutImprovement = 0;
        while (iteration < MAX_ITERATION && iterationsWithoutImprovement < MAX_ITERATIONS_WITHOUT_IMPROVEMENT) {
            List<VRPSolution> neighbors = this.generateNeighbors(currentSolution);
            VRPSolution bestNeighbor = this.findBestNeighbor(neighbors);

            if (bestNeighbor == null) {
                break;
            }

            if (bestNeighbor.calculateScore() > currentSolution.calculateScore()) {
                currentSolution = bestNeighbor;
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

    public VRPSolution initialSolution(VRPSolution solution) {
        Map<Depot, List<Customer>> depotCustomers = assignCustomersToDepots(solution.getDepotList(),
                solution.getCustomerList());
        solution.getVehicleList().sort(Comparator.comparingInt(Vehicle::getCapacity).reversed());
        assignVehiclesToDepots(solution.getVehicleList(), solution.getDepotList(), depotCustomers);

        Set<Customer> assignedCustomers = new HashSet<>();
        for (Vehicle vehicle : solution.getVehicleList()) {
            List<Customer> customers = depotCustomers.get(vehicle.getDepot());
            if (customers != null && !customers.isEmpty()) {
                customers.sort(Comparator.comparingDouble(
                        customer -> vehicle.getDepot().getLocation().getDistanceTo(customer.getLocation())));

                for (Customer customer : customers) {
                    if (!assignedCustomers.contains(customer) && vehicle.canAddCustomer(customer)) {
                        vehicle.addCustomer(customer);
                        assignedCustomers.add(customer);
                    }
                }
            }
        }

        // Ensure that each vehicle has at least one customer
        for (Vehicle vehicle : solution.getVehicleList()) {
            if (vehicle.getCustomerList().isEmpty()) {
                for (Customer customer : solution.getCustomerList()) {
                    if (!assignedCustomers.contains(customer) && vehicle.canAddCustomer(customer)) {
                        vehicle.addCustomer(customer);
                        assignedCustomers.add(customer);
                        break;
                    }
                }
            }
        }

        return solution;
    }

    public List<VRPSolution> generateNeighbors(VRPSolution currentSolution) {
        List<VRPSolution> neighbors = new ArrayList<>();
        neighbors.addAll(generateIntraRouteSwaps(currentSolution));
        neighbors.addAll(generateInterRouteSwaps(currentSolution));
        // neighbors.addAll(generateRelocations(currentSolution));
        return neighbors;
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
     * Generates neighboring solutions by swapping customers within the same route
     */
    private List<VRPSolution> generateIntraRouteSwaps(VRPSolution solution) {
        List<VRPSolution> neighbors = new ArrayList<>();
        for (Vehicle vehicle : solution.getVehicleList()) {
            if (vehicle.getCustomerList().size() < 2)
                continue;
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
     * Generates neighboring solutions by swapping customers between different
     * routes
     */
    private List<VRPSolution> generateInterRouteSwaps(VRPSolution solution) {
        List<VRPSolution> neighbors = new ArrayList<>();
        List<Vehicle> vehicles = solution.getVehicleList();
        for (int i = 0; i < vehicles.size(); i++) {
            for (int j = i + 1; j < vehicles.size(); j++) {
                Vehicle v1 = vehicles.get(i);
                Vehicle v2 = vehicles.get(j);
                if (v1.getCustomerList().isEmpty() || v2.getCustomerList().isEmpty())
                    continue;
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
            if (fromVehicle.getCustomerList().isEmpty())
                continue;
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
     * Relocates a customer from one vehicle to another if capacity constraints are
     * met
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
}
