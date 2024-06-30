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
    public List<Constraint> defineConstraints(Object problem) {
        List<Constraint> constraints = new ArrayList<>();
        constraints.add(vehicleCapacity());
        constraints.add(totalDistance());
        return constraints;
    }

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
