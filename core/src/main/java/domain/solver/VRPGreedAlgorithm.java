package domain.solver;

import domain.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import api.solution.ProblemFactCollectionProperty;

public class VRPGreedAlgorithm {
    Set<Customer> unvisitedCustomers = new HashSet<>();

    public VRPSolution execute(VRPSolution newSolution) {
        List<Vehicle> vehicles = newSolution.getVehicleList();
        List<Customer> customers = newSolution.getCustomerList();
        unvisitedCustomers.addAll(customers);

        for (Vehicle vehicle : vehicles) {
            Location currentLocation = vehicle.getDepot().getLocation();
            while (!unvisitedCustomers.isEmpty() && vehicle.getRemainingCapacity() > 0) {
                Customer nearestCustomer = this.greedRangeValue(currentLocation, unvisitedCustomers);
                if (nearestCustomer == null || !vehicle.canAddCustomer(nearestCustomer))
                    break;
                vehicle.addCustomer(nearestCustomer);
                unvisitedCustomers.remove(nearestCustomer);
                currentLocation = nearestCustomer.getLocation();
            }
        }

        return newSolution;
    }

    public Customer greedRangeValue(Location currentLocation, Set<Customer> unvisitedCustomers) {
        Customer nearestCustomer = null;
        double shortestDistance = Double.MAX_VALUE;

        for (Customer customer : unvisitedCustomers) {
            double distance = currentLocation.getDistanceTo(customer.getLocation());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestCustomer = customer;
            }
        }

        return nearestCustomer;
    }

    public ProblemFactCollectionProperty greedRangeValue() {
        // This method is not used in the current implementation
        return null;
    }
}
