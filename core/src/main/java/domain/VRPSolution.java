package domain;

import api.score.HardSoftScore;
import api.solution.PlanningSolution;
import api.solution.ProblemFactCollectionProperty;
import solver.VehicleRoutingConstraintProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.HashSet;

public class VRPSolution extends PlanningSolution {
    private List<Depot> depotList;
    private List<Customer> customerList;
    private List<Vehicle> vehicleList;

    public VRPSolution() {
        depotList = new ArrayList<>();
        customerList = new ArrayList<>();
        vehicleList = new ArrayList<>();
    }

    public VRPSolution(List<Depot> depotList, List<Customer> customerList, List<Vehicle> vehicleList) {
        this.depotList = new ArrayList<>(depotList);
        this.customerList = new ArrayList<>(customerList);
        this.vehicleList = new ArrayList<>(vehicleList);
    }

    @Override
    public List<? extends ProblemFactCollectionProperty> getValueRange() {
        return customerList;
    }

    @Override
    public double calculateScore() {
        return getHardSoftScore().calculateScore();
    }

    public List<Depot> getDepotList() {
        return new ArrayList<>(depotList);
    }

    public List<Customer> getCustomerList() {
        return new ArrayList<>(customerList);
    }

    public List<Vehicle> getVehicleList() {
        return new ArrayList<>(vehicleList);
    }

    @Override
    protected HardSoftScore<? extends PlanningSolution> getHardSoftScore() {
        return new HardSoftScore<>(this,
                new VehicleRoutingConstraintProvider().defineConstraints());
    }

    @Override
    public VRPSolution clone() {
        return new VRPSolution(depotList, customerList, vehicleList);
    }

    @Override
    public void swap(int pos1, int pos2) {
        Collections.swap(customerList, pos1, pos2);
    }

    @Override
    public void insert(int from, int to) {
        customerList.add(to, customerList.remove(from));
    }

    @Override
    public void invert(int start, int end) {
        Collections.reverse(customerList.subList(start, end));
    }

    @Override
    public boolean canAddElement(Object element) {
        return true;
    }

    @Override
    public void addElement(Object element) {
        customerList.add((Customer) element);
    }

    @Override
    public void initialize() {
        Map<Depot, List<Customer>> depotCustomers = assignCustomersToDepots(this.depotList, this.customerList);
        this.vehicleList.sort(Comparator.comparingInt(Vehicle::getCapacity).reversed());
        assignVehiclesToDepots(this.vehicleList, this.depotList, depotCustomers);

        Set<Customer> assignedCustomers = new HashSet<>();
        for (Vehicle vehicle : this.getVehicleList()) {
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

        for (Vehicle vehicle : this.getVehicleList()) {
            if (vehicle.getCustomerList().isEmpty()) {
                for (Customer customer : this.getCustomerList()) {
                    if (!assignedCustomers.contains(customer) && vehicle.canAddCustomer(customer)) {
                        vehicle.addCustomer(customer);
                        assignedCustomers.add(customer);
                        break;
                    }
                }
            }
        }
    }

    private Map<Depot, List<Customer>> assignCustomersToDepots(List<Depot> depots, List<Customer> customers) {
        Map<Depot, List<Customer>> result = depots.stream()
                .collect(Collectors.toMap(depot -> depot, depot -> new ArrayList<>()));

        customers.forEach(customer -> result.get(findNearestDepot(customer, depots)).add(customer));

        return result;
    }

    private Depot findNearestDepot(Customer customer, List<Depot> depots) {
        return depots.stream()
                .min(Comparator.comparingDouble(depot -> customer.getLocation().getDistanceTo(depot.getLocation())))
                .orElseThrow(() -> new RuntimeException("No depot found"));
    }

    private void assignVehiclesToDepots(List<Vehicle> vehicles, List<Depot> depots,
            Map<Depot, List<Customer>> depotCustomers) {
        Map<Depot, Integer> depotTotalDemand = calculateDepotTotalDemand(depotCustomers);

        vehicles.forEach(vehicle -> {
            Depot bestDepot = depots.stream()
                    .min(Comparator.comparingInt(
                            depot -> Math.abs(vehicle.getCapacity() - depotTotalDemand.getOrDefault(depot, 0))))
                    .orElse(depots.get(0));

            vehicle.setDepot(bestDepot);
            depotTotalDemand.put(bestDepot, depotTotalDemand.getOrDefault(bestDepot, 0) - vehicle.getCapacity());
        });
    }

    private Map<Depot, Integer> calculateDepotTotalDemand(Map<Depot, List<Customer>> depotCustomers) {
        return depotCustomers.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().mapToInt(Customer::getDemand).sum()));
    }
}
