package domain;

import api.score.HardSoftScore;
import api.solution.PlanningSolution;
import api.solution.ProblemFactCollectionProperty;
import solver.VehicleRoutingConstraintProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VRPSolution implements PlanningSolution, Cloneable {
    private List<Depot> depotList;
    private List<Customer> customerList;
    private List<Vehicle> vehicleList;
    private HardSoftScore hardSoftScore;

    public VRPSolution() {
        // Default constructor
    }

    public VRPSolution(List<Depot> depotList, List<Customer> customerList, List<Vehicle> vehicleList) {
        this.depotList = new ArrayList<>(depotList);
        this.customerList = new ArrayList<>(customerList);
        this.vehicleList = new ArrayList<>(vehicleList);
        this.hardSoftScore = new HardSoftScore(this, new VehicleRoutingConstraintProvider().defineConstraints());
        assignDepotsToVehicles();
    }

    private void assignDepotsToVehicles() {
        for (int i = 0; i < vehicleList.size(); i++) {
            Vehicle vehicle = vehicleList.get(i);
            Depot depot = depotList.get(i % depotList.size());
            vehicle.setDepot(depot);
        }
    }

    @Override
    public List<? extends ProblemFactCollectionProperty> getValueRange() {
        return customerList;
    }

    @Override
    public double calculateScore() {
        return hardSoftScore.calculateScore();
    }

    @Override
    public VRPSolution clone() {
        try {
            VRPSolution clone = (VRPSolution) super.clone();
            clone.depotList = depotList.stream()
                    .map(depot -> new Depot(depot.getId(), depot.getLocation()))
                    .collect(Collectors.toList());
            clone.customerList = customerList.stream()
                    .map(customer -> new Customer(customer.getId(), customer.getLocation(), customer.getDemand()))
                    .collect(Collectors.toList());
            clone.vehicleList = vehicleList.stream()
                    .map(Vehicle::clone)
                    .collect(Collectors.toList());
            clone.hardSoftScore = new HardSoftScore(clone, new VehicleRoutingConstraintProvider().defineConstraints());
            clone.assignDepotsToVehicles();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning should be supported", e);
        }
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
}
