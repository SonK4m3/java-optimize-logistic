package domain.solver;

import api.score.HardSoftScore;
import api.solution.PlanningSolution;
import api.score.ScoreCalculator;
import domain.Customer;
import domain.Depot;
import domain.VRPSolution;
import domain.Vehicle;
import domain.display.ConsoleDisplay;
import domain.display.Display;

import java.util.*;

public class VRPSolver implements TabuSearch<VRPSolution> {
    private final static int MAX_ITERATION = 10000;
    private final int tabuListSize;
    private final Queue<PlanningSolution> tabuList = new LinkedList<>();
    private final ScoreCalculator scoreCalculator = new ScoreCalculator();
    Display display = new ConsoleDisplay();

    public VRPSolver(int tabuListSize) {
        this.tabuListSize = tabuListSize;
    }

    public VRPSolution solve(VRPSolution initialSolution) {
        VRPSolution currentSolution = (VRPSolution) initialSolution.clone();
        tabuList.add(initialSolution);

        VRPSolution bestSolution = (VRPSolution) initialSolution.clone();

        int iteration = 0;
        while (iteration < MAX_ITERATION) {
            List<VRPSolution> neighbors = this.generateNeighbors(currentSolution);
            neighbors.removeIf(tabuList::contains);
            VRPSolution bestNeighbor = neighbors.stream()
                    .min(Comparator.comparingDouble(VRPSolution::calculateScore))
                    .orElse(null);

            if (bestNeighbor == null) {
                break;
            }

            currentSolution = (VRPSolution) bestNeighbor.clone();
            if (currentSolution.calculateScore() < bestSolution.calculateScore()) {
                bestSolution = (VRPSolution) currentSolution.clone();

                System.out.println("--- step: " + iteration);
                display.displaySolution(bestSolution);
            }

            tabuList.add(currentSolution);
            if (tabuList.size() > tabuListSize) {
                tabuList.remove();
            }

            iteration++;
        }

        return bestSolution;
    }

    private Map<Depot, List<Customer>> assignCustomersToDepots(List<Depot> depots, List<Customer> customers) {
        Map<Depot, List<Customer>> depotCustomers = new HashMap<>();
        for (Depot depot : depots) {
            depotCustomers.put(depot, new ArrayList<>());
        }

        for (Customer customer : customers) {
            Depot closestDepot = depots.get(0);
            double minDistance = Double.MAX_VALUE;
            for (Depot depot : depots) {
                double distance = customer.getLocation().getDistanceTo(depot.getLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    closestDepot = depot;
                }
            }
            depotCustomers.get(closestDepot).add(customer);
        }

        return depotCustomers;
    }

    /**
     * Vehicles are assigned to the depot that has contains enough the customer demands
     *
     * @param vehicles       the list of vehicles
     * @param depots         the list of depots
     * @param depotCustomers the list of customers clustering follow depots
     */
    private void assignVehiclesToDepots(List<Vehicle> vehicles, List<Depot> depots, Map<Depot, List<Customer>> depotCustomers) {
        // calculate the total demand of customers' depots
        Map<Depot, Integer> depotTotalDemand = new HashMap<>();
        for (Depot depot : depots) {
            depotTotalDemand.put(
                    depot,
                    depotCustomers.get(depot).stream()
                            .mapToInt(Customer::getDemand)
                            .sum()
            );
        }

        for (Vehicle vehicle : vehicles) {
            Depot bestDepot = null;
            int minTotalDemandDifference = Integer.MAX_VALUE;

            for (Depot depot : depots) {
                int demandDifference = Math.abs(vehicle.getCapacity() - depotTotalDemand.get(depot));
                if (demandDifference < minTotalDemandDifference) {
                    minTotalDemandDifference = demandDifference;
                    bestDepot = depot;
                }
            }

            vehicle.setDepot(bestDepot);
            depotTotalDemand.put(bestDepot, depotTotalDemand.get(bestDepot) - vehicle.getCapacity());
        }
    }

    @Override
    public VRPSolution initialSolution(VRPSolution rawSolution) {
        Map<Depot, List<Customer>> depotCustomers = assignCustomersToDepots(
                rawSolution.getDepotList(),
                rawSolution.getCustomerList()
        );

        rawSolution.getVehicleList().sort(Comparator.comparingInt(v -> -v.getCapacity()));

        assignVehiclesToDepots(rawSolution.getVehicleList(), rawSolution.getDepotList(), depotCustomers);

        Set<Customer> assignedCustomers = new HashSet<>();

        for (Vehicle vehicle : rawSolution.getVehicleList()) {
            List<Customer> customers = depotCustomers.get(vehicle.getDepot());
            customers.sort(
                    Comparator.comparingDouble(customer ->
                            vehicle.getDepot().getLocation()
                                    .getDistanceTo(customer.getLocation())
                    )
            );

            // add un-assigned customers to the vehicle
            for (Customer customer : customers) {
                if (!assignedCustomers.contains(customer) && vehicle.canAddCustomer(customer)) {
                    vehicle.getCustomerList().add(customer);
                    assignedCustomers.add(customer);
                }
            }
        }

        return rawSolution;
    }

    @Override
    public List<VRPSolution> generateNeighbors(VRPSolution currentSolution) {
        List<VRPSolution> neighbors = new ArrayList<>();

        currentSolution.getVehicleList().forEach(v -> {
            // Swap customers between routes (ensuring depot remains first)
            for (int i = 0; i < v.getCustomerList().size(); i++) {
                for (int j = i + 1; j < v.getCustomerList().size(); j++) {
                    VRPSolution neighbor = (VRPSolution) currentSolution.clone();

                    Customer temp = v.getCustomerList().get(i);
                    v.getCustomerList().set(i, v.getCustomerList().get(j));
                    v.getCustomerList().set(j, temp);
                    neighbors.add(neighbor);
                }
            }
        });

        // 2. Hoán vị khách hàng giữa các xe (Inter-route Swaps)
        for (int i = 0; i < currentSolution.getVehicleList().size(); i++) {
            for (int j = i + 1; j < currentSolution.getVehicleList().size(); j++) {
                Vehicle v1 = currentSolution.getVehicleList().get(i);
                Vehicle v2 = currentSolution.getVehicleList().get(j);
                for (int k = 0; k < v1.getCustomerList().size(); k++) { // Bắt đầu từ 1 để tránh depot
                    for (int l = 0; l < v2.getCustomerList().size(); l++) {
                        VRPSolution neighbor = (VRPSolution) currentSolution.clone();
                        Vehicle clonedV1 = getVehicleById(neighbor.getVehicleList(), v1.getId());
                        Vehicle clonedV2 = getVehicleById(neighbor.getVehicleList(), v2.getId());
                        swapCustomersBetweenVehicles(clonedV1, clonedV2, k, l);
                        neighbors.add(neighbor);
                    }
                }
            }
        }

        // 3. Chèn khách hàng vào một tuyến đường khác (Relocation)
        for (Vehicle vehicle : currentSolution.getVehicleList()) {
            for (int i = 0; i < vehicle.getCustomerList().size(); i++) {
                Customer customer = vehicle.getCustomerList().get(i);
                for (Vehicle otherVehicle : currentSolution.getVehicleList()) {
                    if (vehicle != otherVehicle) {
                        for (int j = 0; j <= otherVehicle.getCustomerList().size(); j++) {
                            VRPSolution neighbor = (VRPSolution) currentSolution.clone();

                            Vehicle clonedVehicle = vehicle.clone();
                            Vehicle clonedOtherVehicle = otherVehicle.clone();
                            relocateCustomer(clonedVehicle, clonedOtherVehicle, customer, i, j);
                            neighbors.add(neighbor);
                        }
                    }
                }
            }
        }

        return neighbors;
    }

    private void swapCustomersBetweenVehicles(Vehicle v1, Vehicle v2, int index1, int index2) {
        if (index1 < 0 || index1 >= v1.getCustomerList().size() || index2 < 0 || index2 >= v2.getCustomerList().size()) {
            throw new IllegalArgumentException("Invalid customer indices");
        }

        Customer customer1 = v1.getCustomerList().get(index1);
        Customer customer2 = v2.getCustomerList().get(index2);

        v1.getCustomerList().set(index1, customer2);
        v2.getCustomerList().set(index2, customer1);
    }

    // Phương thức tìm Vehicle theo ID
    private Vehicle getVehicleById(List<Vehicle> vehicles, long id) {
        return vehicles.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }

    // Phương thức di chuyển một khách hàng sang một tuyến đường khác
    private void relocateCustomer(Vehicle fromVehicle, Vehicle toVehicle, Customer customer, int fromIndex, int toIndex) {
        fromVehicle.getCustomerList().remove(fromIndex);
        toVehicle.getCustomerList().add(toIndex, customer);
    }
}
