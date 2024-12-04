package solver;

import api.stream.Constraint;
import api.stream.ConstraintProvider;
import domain.Customer;
import domain.VRPSolution;
import domain.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleRoutingConstraintProvider implements ConstraintProvider<VRPSolution> {

    @Override
    public List<Constraint<VRPSolution>> defineConstraints() {
        List<Constraint<VRPSolution>> constraints = new ArrayList<>();
        constraints.add(vehicleCapacity());
        constraints.add(totalDistance());
        return constraints;
    }

    /**
     * Checks if the vehicle capacity constraint is satisfied.
     * 
     * This constraint ensures that the total demand of customers assigned to any
     * vehicle does not exceed the vehicle's capacity.
     * 
     * @return A Constraint object representing the vehicle capacity constraint.
     */
    private Constraint<VRPSolution> vehicleCapacity() {
        return new Constraint<VRPSolution>() {
            @Override
            public boolean isSatisfied(VRPSolution solution) {
                for (Vehicle vehicle : solution.getVehicleList()) {
                    int totalDemand = vehicle.getCustomerList().stream()
                            .mapToInt(Customer::getDemand)
                            .sum();
                    if (totalDemand > vehicle.getCapacity()) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public double getScore(VRPSolution solution) {
                return isSatisfied(solution) ? 0 : Double.POSITIVE_INFINITY;
            }
        };
    }

    /**
     * Calculates the total distance of all vehicles in the solution.
     * 
     * This constraint is used to ensure that the total distance traveled by all
     * vehicles is minimized.
     * 
     * @return A Constraint object representing the total distance constraint.
     */
    private Constraint<VRPSolution> totalDistance() {
        return new Constraint<VRPSolution>() {
            @Override
            public boolean isSatisfied(VRPSolution solution) {
                return true;
            }

            @Override
            public double getScore(VRPSolution solution) {
                if (solution.getVehicleList() == null || solution.getVehicleList().isEmpty()) {
                    return Double.POSITIVE_INFINITY;
                }

                return solution.getVehicleList().stream()
                        .mapToDouble(Vehicle::getTotalDistanceMeters)
                        .sum();
            }
        };
    }
}
