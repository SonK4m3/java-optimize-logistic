package solver;

import api.solution.PlanningSolution;
import api.stream.Constraint;
import api.stream.ConstraintProvider;
import domain.Customer;
import domain.VRPSolution;
import domain.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleRoutingConstraintProvider implements ConstraintProvider {

    @Override
    public List<Constraint> defineConstraints() {
        List<Constraint> constraints = new ArrayList<>();
        constraints.add(vehicleCapacity());
        constraints.add(totalDistance());
        return constraints;
    }

    /**
     * Checks if the vehicle capacity constraint is satisfied.
     * 
     * This constraint ensures that the total demand of customers assigned to any vehicle does not exceed the vehicle's capacity.
     * 
     * @return A Constraint object representing the vehicle capacity constraint.
     */
    private Constraint vehicleCapacity() {
        return new Constraint() {
            @Override
            public boolean isSatisfied(PlanningSolution solution) {
                VRPSolution vrpInstance = (VRPSolution) solution;
                for (Vehicle vehicle : vrpInstance.getVehicleList()) {
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
            public double getScore(PlanningSolution solution) {
                return isSatisfied(solution) ? 0 : Double.POSITIVE_INFINITY;
            }
        };
    }

    /**
     * Calculates the total distance of all vehicles in the solution.
     * 
     * This constraint is used to ensure that the total distance traveled by all vehicles is minimized.
     * 
     * @return A Constraint object representing the total distance constraint.
     */
    private Constraint totalDistance() {
        return new Constraint() {
            @Override
            public boolean isSatisfied(PlanningSolution solution) {
                return true;
            }

            @Override
            public double getScore(PlanningSolution solution) {
                VRPSolution vrpInstance = (VRPSolution) solution;
                if (vrpInstance.getVehicleList() == null || vrpInstance.getVehicleList().isEmpty()) {
                    return Double.POSITIVE_INFINITY;
                }

                return vrpInstance.getVehicleList().stream()
                        .mapToDouble(Vehicle::getTotalDistanceMeters)
                        .sum();
            }
        };
    }
}
