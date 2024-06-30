package domain;

import api.score.HardSoftScore;
import api.solution.PlanningSolution;
import api.solution.ProblemFactCollectionProperty;
import domain.geo.EuclideanDistanceCalculator;
import solver.VehicleRoutingConstraintProvider;

import java.util.ArrayList;
import java.util.List;

public class VRPSolution implements PlanningSolution, Cloneable {
    private List<Depot> depotList;
    private List<Customer> customerList;
    private List<Vehicle> vehicleList;
    private HardSoftScore hardSoftScore;

    public VRPSolution() {

    }

    public VRPSolution(List<Depot> depotList, List<Customer> customerList, List<Vehicle> vehicleList) {
        this.depotList = depotList;
        this.customerList = customerList;
        this.vehicleList = vehicleList;
        this.hardSoftScore = new HardSoftScore(this, new VehicleRoutingConstraintProvider().defineConstraints(this));
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
    public Object clone() {
        try {
            VRPSolution clone = (VRPSolution) super.clone();

            clone.depotList = new ArrayList<>();
            for (Depot depot : depotList) {
                clone.depotList.add(new Depot(depot.getId(), depot.getLocation()));
            }

            clone.customerList = new ArrayList<>();
            for (Customer customer : customerList) {
                clone.customerList.add(new Customer(customer.getId(), customer.getLocation(), customer.getDemand()));
            }

            clone.vehicleList = new ArrayList<>();
            for (Vehicle vehicle : vehicleList) {
                clone.vehicleList.add(vehicle.clone());
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public List<Depot> getDepotList() {
        return depotList;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }
}
