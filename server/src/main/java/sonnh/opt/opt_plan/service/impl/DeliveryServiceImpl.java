package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.VehicleType;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.*;
import sonnh.opt.opt_plan.repository.*;
import sonnh.opt.opt_plan.service.DeliveryService;
import sonnh.opt.opt_plan.payload.request.DeliveryUpdateRequest;
import sonnh.opt.opt_plan.payload.response.DeliveryMetrics;
import sonnh.opt.opt_plan.payload.response.SuggestDriversResponse;
import sonnh.opt.opt_plan.payload.dto.DriverDTO;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

        private final double maxPickupDistance = 10.0;
        private final double maxDeliveryDistance = 50.0;

        private final DeliveryRepository deliveryRepository;
        private final OrderRepository orderRepository;
        private final DriverRepository driverRepository;
        private final WarehouseRepository warehouseRepository;

        @Override
        @Transactional(readOnly = true)
        public Delivery getDeliveriesByOrderId(Long orderId) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Order not found with id: " + orderId));
                return deliveryRepository.findByOrder(order);
        }

        @Override
        @Transactional
        public Delivery updateDeliveryStatus(Long deliveryId,
                        DeliveryUpdateRequest request) {
                Delivery delivery = deliveryRepository.findByIdWithHistory(deliveryId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Delivery not found with id: "
                                                                + deliveryId));

                delivery = this.updateStatus(delivery, request.getStatus(),
                                request.getNote());

                // Update driver status if delivery is completed
                if (request.getStatus() == DeliveryStatus.DELIVERED) {
                        Driver driver = delivery.getDriver();
                        if (driver != null) {
                                driver.setStatus(DriverStatus.AVAILABLE);
                                driverRepository.save(driver);
                        }
                }

                return deliveryRepository.save(delivery);
        }

        @Override
        @Transactional
        public Delivery assignDriver(Long deliveryId, Long driverId) {
                Delivery delivery = deliveryRepository.findById(deliveryId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Delivery not found with id: "
                                                                + deliveryId));

                Driver driver = driverRepository.findById(driverId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Driver not found with id: " + driverId));

                if (driver.getStatus() != DriverStatus.AVAILABLE) {
                        throw new IllegalStateException("Driver is not available");
                }

                delivery = this.assignDriver(delivery, driver);

                return deliveryRepository.save(delivery);
        }

        @Override
        @Transactional(readOnly = true)
        public List<Driver> getAvailableDrivers() {
                return driverRepository.findByStatus(DriverStatus.AVAILABLE);
        }

        @Override
        @Transactional
        public void updateDriverLocation(Long driverId, Double latitude,
                        Double longitude) {
                Driver driver = driverRepository.findById(driverId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Driver not found with id: " + driverId));

                driver.updateLocation(latitude, longitude);
                driverRepository.save(driver);
        }

        @Override
        @Transactional(readOnly = true)
        public DeliveryMetrics getDeliveryMetrics(Long deliveryId) {
                Delivery delivery = deliveryRepository.findById(deliveryId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Delivery not found with id: "
                                                                + deliveryId));

                return DeliveryMetrics.builder().deliveryId(delivery.getId())
                                .status(delivery.getStatus())
                                .estimatedDistance(delivery.getEstimatedDistance())
                                .estimatedDeliveryTime(
                                                delivery.getEstimatedDeliveryTime())
                                .actualDeliveryTime(delivery.getActualDeliveryTime())
                                .driverName(delivery.getDriver() != null ? delivery
                                                .getDriver().getUser().getFullName()
                                                : null)
                                .driverRating(delivery.getDriver() != null
                                                ? delivery.getDriver().getRating()
                                                : null)
                                .build();
        }

        @Override
        @Transactional
        public List<Delivery> optimizeRoutes(List<Long> deliveryIds) {
                // Implement route optimization logic here
                // This is a placeholder implementation
                return deliveryRepository.findAllById(deliveryIds);
        }

        @Override
        public List<Driver> getAvailableDriversForDelivery(Long deliveryId) {
                Delivery delivery = deliveryRepository.findById(deliveryId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Delivery not found with id: "
                                                                + deliveryId));

                Order order = delivery.getOrder();
                if (order == null) {
                        throw new ResourceNotFoundException(
                                        "Order not found for delivery");
                }

                // Get order weight and delivery location
                Double orderWeight = order.getTotalWeight();
                Location deliveryLocation = delivery.getDeliveryLocation();

                // Get vehicle type if driver is assigned
                VehicleType vehicleType = VehicleType.getRequiredVehicleType(orderWeight);

                // Get warehouses and handle not found efficiently
                List<Warehouse> warehouses = delivery.getWarehouseList().stream()
                                .map(id -> warehouseRepository.findById(id).orElseThrow(
                                                () -> new ResourceNotFoundException(String
                                                                .format("Warehouse not found with id: %d",
                                                                                id))))
                                .toList();

                return findAvailableDriversByVehicleType(vehicleType, deliveryLocation,
                                warehouses, maxPickupDistance, maxDeliveryDistance);
        }

        private List<Driver> findAvailableDriversByVehicleType(VehicleType vehicleType,
                        Location deliveryLocation, List<Warehouse> warehouses,
                        double maxPickupDistance, double maxDeliveryDistance) {
                List<Driver> drivers = driverRepository.findByStatusAndVehicleType(
                                DriverStatus.AVAILABLE, vehicleType);

                return drivers.stream()
                                .filter(driver -> driver.getCurrentLatitude() != null
                                                && driver.getCurrentLongitude() != null
                                                && driver.getCurrentLatitude() != 0.0
                                                && driver.getCurrentLongitude() != 0.0)
                                .collect(Collectors.toList());
        }

        private Delivery assignDriver(Delivery delivery, Driver driver) {
                delivery.setDriver(driver);
                driver.setStatus(DriverStatus.BUSY);
                driverRepository.save(driver);
                return delivery;
        }

        private Delivery updateStatus(Delivery delivery, DeliveryStatus status,
                        String note) {
                delivery.setStatus(status);
                delivery.setUpdatedAt(LocalDateTime.now());
                return delivery;
        }

        @Override
        public SuggestDriversResponse suggestDriversForDelivery(Long deliveryId,
                        Long driverNumber, List<Long> driverIds) {
                Delivery delivery = getDeliveryOrThrow(deliveryId);
                List<Driver> drivers = getDriversOrThrow(driverIds);
                SuggestDriversResponse response = SuggestDriversResponse.builder()
                                .build();

                // Get required vehicle type based on order weight
                Double orderWeight = delivery.getOrder().getTotalWeight();
                VehicleType requiredVehicleType = VehicleType
                                .getRequiredVehicleType(orderWeight);

                // Filter available drivers with matching vehicle type
                List<Driver> availableDrivers = drivers.stream().filter(driver -> driver
                                .getStatus() == DriverStatus.AVAILABLE
                                && driver.getVehicleType() == requiredVehicleType)
                                .limit(driverNumber).collect(Collectors.toList());
                // Filter warehouses with matching vehicle type
                List<Warehouse> matchingWarehouses = warehouseRepository
                                .findAllById(delivery.getWarehouseList()).stream()
                                .collect(Collectors.toList());

                // Ensure we have enough warehouses
                if (matchingWarehouses.isEmpty()) {
                        return response; // Return empty response if no matching
                                         // warehouses
                }

                // Distribute warehouses among drivers
                int warehousesPerDriver = Math.max(1,
                                matchingWarehouses.size() / availableDrivers.size());

                for (int i = 0; i < availableDrivers.size(); i++) {
                        Driver driver = availableDrivers.get(i);
                        List<Long> warehouseIds = new ArrayList<>();

                        // Calculate start and end index for this driver's
                        // warehouses
                        int start = i * warehousesPerDriver;
                        int end = Math.min((i + 1) * warehousesPerDriver,
                                        matchingWarehouses.size());

                        // If this is the last driver, assign any remaining
                        // warehouses
                        if (i == availableDrivers.size() - 1) {
                                end = matchingWarehouses.size();
                        }

                        // Assign warehouses to this driver
                        for (int j = start; j < end; j++) {
                                warehouseIds.add(matchingWarehouses.get(j).getId());
                        }

                        // If driver has no warehouses yet, assign the first
                        // warehouse
                        if (warehouseIds.isEmpty() && !matchingWarehouses.isEmpty()) {
                                warehouseIds.add(matchingWarehouses.get(0).getId());
                        }

                        response.addDriver(DriverDTO.fromEntity(driver), warehouseIds);
                }
                return response;
        }

        private Delivery getDeliveryOrThrow(Long deliveryId) {
                return deliveryRepository.findById(deliveryId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Delivery not found with id: "
                                                                + deliveryId));
        }

        private List<Driver> getDriversOrThrow(List<Long> driverIds) {
                return driverRepository.findAllById(driverIds);
        }

        private List<Warehouse> getWarehousesOrThrow(List<Long> warehouseIds) {
                return warehouseRepository.findAllById(warehouseIds);
        }
}