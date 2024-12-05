package sonnh.opt.opt_plan.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import api.score.HardSoftScore;
import api.solution.PlanningSolution;
import api.solution.ProblemFactCollectionProperty;
import api.stream.ConstraintProvider;
import api.stream.Constraint;

import lombok.Data;

@Data
public class VRPSolution implements PlanningSolution {
    private static final int MAX_ITERATIONS = 100;
    private static final double CONVERGENCE_THRESHOLD = 1e-6;

    private List<Warehouse> warehouses;
    private List<OrderDetail> orderDetails;
    private List<Driver> drivers;
    private List<WarehouseDriver> warehouseDrivers;
    private Long numberDrivers;

    public VRPSolution(List<Warehouse> warehouses, List<OrderDetail> orderDetails, List<Driver> drivers,
            Long numberDrivers) {
        this.warehouses = new ArrayList<>(warehouses);
        this.orderDetails = new ArrayList<>(orderDetails);
        this.drivers = new ArrayList<>(drivers);
        this.warehouseDrivers = new ArrayList<>();
        this.numberDrivers = numberDrivers;
    }

    @Override
    public List<? extends ProblemFactCollectionProperty> getValueRange() {
        return orderDetails.stream()
                .map(OrderDetail::getOrder)
                .map(Order::getCustomer)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public double calculateScore() {
        return getHardSoftScore().calculateScore();
    }

    @Override
    public HardSoftScore<? extends PlanningSolution> getHardSoftScore() {
        return new HardSoftScore<VRPSolution>(this,
                new ConstraintProvider<VRPSolution>() {
                    @Override
                    public List<Constraint<VRPSolution>> defineConstraints() {
                        return Arrays.asList(vehicleCapacityConstraint(), totalDistance());
                    }
                }.defineConstraints());
    }

    @Override
    public boolean canAddElement(Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canAddElement'");
    }

    @Override
    public void addElement(Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addElement'");
    }

    @Override
    public void swap(int pos1, int pos2) {
        WarehouseDriver warehouseDriver1 = warehouseDrivers.get(pos1);
        WarehouseDriver warehouseDriver2 = warehouseDrivers.get(pos2);

        Driver driver1 = warehouseDriver1.getDriver();
        Driver driver2 = warehouseDriver2.getDriver();

        warehouseDriver1.setDriver(driver2);
        warehouseDriver2.setDriver(driver1);
    }

    @Override
    public void insert(int from, int to) {

    }

    @Override
    public void invert(int start, int end) {

    }

    @Override
    public PlanningSolution clone() {
        VRPSolution clone = new VRPSolution(warehouses, orderDetails, drivers, numberDrivers);
        clone.setWarehouseDrivers(warehouseDrivers.stream()
                .map(WarehouseDriver::clone)
                .collect(Collectors.toList()));
        return clone;
    }

    @Override
    public void initialize() {
        Map<Driver, List<Warehouse>> driverClusters = clusterWarehousesForDrivers();

        createDeliveryAssignmentsFromClusters(driverClusters);
    }

    private Constraint<VRPSolution> vehicleCapacityConstraint() {
        return new Constraint<VRPSolution>() {
            @Override
            public boolean isSatisfied(VRPSolution solution) {
                return solution.getDrivers().stream().allMatch(driver -> {
                    double totalWeight = driver.getDeliveries().stream()
                            .mapToDouble(delivery -> delivery.getOrder().getTotalWeight())
                            .sum();
                    return driver.getVehicleType().getMaxWeight() >= totalWeight;
                });
            }

            @Override
            public double getScore(VRPSolution solution) {
                return isSatisfied(solution) ? 0 : Double.POSITIVE_INFINITY;
            }
        };
    }

    private Constraint<VRPSolution> totalDistance() {
        return new Constraint<VRPSolution>() {
            @Override
            public boolean isSatisfied(VRPSolution solution) {
                return true;
            }

            @Override
            public double getScore(VRPSolution solution) {
                return 0;
            }
        };
    }

    private Map<Driver, List<Warehouse>> clusterWarehousesForDrivers() {
        Long k = numberDrivers; // Number of clusters equals number of drivers
        Map<Driver, Location> centroids = initializeCentroids();
        Map<Driver, List<Warehouse>> clusters = new HashMap<>();

        double prevCost = Double.MAX_VALUE;
        int iteration = 0;

        while (iteration < MAX_ITERATIONS) {
            // Assign warehouses to nearest centroid
            clusters.clear();
            drivers.forEach(driver -> clusters.put(driver, new ArrayList<>()));

            for (Warehouse warehouse : warehouses) {
                Driver nearestDriver = findNearestDriver(warehouse, centroids);
                clusters.get(nearestDriver).add(warehouse);
            }

            // Update centroids
            updateCentroids(clusters, centroids);

            // Calculate current cost
            double currentCost = calculateClusteringCost(clusters, centroids);

            // Check convergence
            if (Math.abs(prevCost - currentCost) < CONVERGENCE_THRESHOLD) {
                break;
            }

            prevCost = currentCost;
            iteration++;
        }

        return clusters;
    }

    private void createDeliveryAssignmentsFromClusters(Map<Driver, List<Warehouse>> clusters) {
        warehouseDrivers.clear();

        clusters.forEach((driver, warehouses) -> {
            List<Long> warehouseIds = warehouses.stream()
                    .map(Warehouse::getId)
                    .collect(Collectors.toList());

            WarehouseDriver warehouseDriver = WarehouseDriver.builder()
                    .driver(driver)
                    .warehouseIds(warehouseIds)
                    .build();
            warehouseDrivers.add(warehouseDriver);
        });
    }

    private Map<Driver, Location> initializeCentroids() {
        Map<Driver, Location> centroids = new HashMap<>();
        Random random = new Random();

        // Initialize centroids with random warehouse locations
        for (Driver driver : drivers) {
            int randomIndex = random.nextInt(warehouses.size());
            Warehouse randomWarehouse = warehouses.get(randomIndex);

            Location centroid = new Location();
            centroid.setLatitude(randomWarehouse.getLocation().getLatitude());
            centroid.setLongitude(randomWarehouse.getLocation().getLongitude());
            centroids.put(driver, centroid);
        }

        return centroids;
    }

    private Driver findNearestDriver(Warehouse warehouse, Map<Driver, Location> centroids) {
        Location warehousePoint = new Location();
        warehousePoint.setLatitude(warehouse.getLocation().getLatitude());
        warehousePoint.setLongitude(warehouse.getLocation().getLongitude());

        return centroids.entrySet().stream()
                .min((e1, e2) -> {
                    double dist1 = e1.getValue().getDistance(warehousePoint);
                    double dist2 = e2.getValue().getDistance(warehousePoint);
                    return Double.compare(dist1, dist2);
                })
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException("No nearest driver found"));
    }

    private void updateCentroids(Map<Driver, List<Warehouse>> clusters, Map<Driver, Location> centroids) {
        clusters.forEach((driver, warehouses) -> {
            if (!warehouses.isEmpty()) {
                double avgLat = warehouses.stream()
                        .mapToDouble(warehouse -> warehouse.getLocation().getLatitude())
                        .average()
                        .orElse(0.0);

                double avgLon = warehouses.stream()
                        .mapToDouble(warehouse -> warehouse.getLocation().getLongitude())
                        .average()
                        .orElse(0.0);

                Location newCentroid = new Location();
                newCentroid.setLatitude(avgLat);
                newCentroid.setLongitude(avgLon);
                centroids.put(driver, newCentroid);
            }
        });
    }

    private double calculateClusteringCost(Map<Driver, List<Warehouse>> clusters, Map<Driver, Location> centroids) {
        return clusters.entrySet().stream()
                .mapToDouble(entry -> {
                    Driver driver = entry.getKey();
                    List<Warehouse> warehouses = entry.getValue();
                    Location centroid = centroids.get(driver);

                    return warehouses.stream()
                            .mapToDouble(warehouse -> {
                                Location warehousePoint = new Location();
                                warehousePoint.setLatitude(warehouse.getLocation().getLatitude());
                                warehousePoint.setLongitude(warehouse.getLocation().getLongitude());
                                return centroid.getDistance(warehousePoint);
                            })
                            .sum();
                })
                .sum();
    }
}
