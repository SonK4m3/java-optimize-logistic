package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.constant.enums.DriverStatus;
import sonnh.opt.opt_plan.constant.enums.VehicleType;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.*;
import sonnh.opt.opt_plan.repository.*;
import sonnh.opt.opt_plan.service.DeliveryService;
import sonnh.opt.opt_plan.service.OrderService;
import sonnh.opt.opt_plan.payload.response.DeliveryMetrics;
import sonnh.opt.opt_plan.payload.response.SuggestDriversResponse;
import sonnh.opt.opt_plan.payload.dto.DriverDTO;

import api.solver.Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
        private final DeliveryRepository deliveryRepository;
        private final WarehouseRepository warehouseRepository;
        private final DriverRepository driverRepository;

        private final OrderService orderService;

        @Override
        @Transactional(readOnly = true)
        public Delivery getDeliveryByOrderId(Long orderId) {
                Order order = orderService.getOrderById(orderId);
                return deliveryRepository.findByOrder(order);
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
        public SuggestDriversResponse suggestDriversForDelivery(Long deliveryId,
                        Long driverNumber, List<Long> driverIds) {
                Delivery delivery = getDeliveryOrThrow(deliveryId);
                List<Driver> drivers = this.getDriversOrThrow(driverIds);
                SuggestDriversResponse response = SuggestDriversResponse.builder()
                                .build();

                // Get required vehicle type based on order weight
                Double orderWeight = delivery.getOrder().getTotalWeight();
                VehicleType requiredVehicleType = VehicleType
                                .getRequiredVehicleType(orderWeight);

                // Filter available drivers with matching vehicle type
                List<Driver> availableDrivers = drivers.stream().filter(driver -> driver
                                .getStatus() == DriverStatus.READY_TO_ACCEPT_ORDERS
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

        @Override
        public Delivery getDeliveryOrThrow(Long deliveryId) {
                return deliveryRepository.findById(deliveryId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Delivery not found with id: "
                                                                + deliveryId));
        }

        private List<Driver> getDriversOrThrow(List<Long> driverIds) {
                List<Driver> drivers = driverRepository.findAllById(driverIds);
                if (drivers == null || drivers.isEmpty()) {
                        throw new ResourceNotFoundException(
                                        "Drivers not found with ids: " + driverIds);
                }
                return drivers;
        }

        @Override
        public SuggestDriversResponse suggestDriversForDeliveryVRP(Long deliveryId, Long driverNumber,
                        List<Long> driverIds) {

                Delivery delivery = getDeliveryOrThrow(deliveryId);
                List<Driver> drivers = this.getDriversOrThrow(driverIds);
                SuggestDriversResponse response = SuggestDriversResponse.builder()
                                .build();

                // Get required vehicle type based on order weight
                Double orderWeight = delivery.getOrder().getTotalWeight();
                VehicleType requiredVehicleType = VehicleType
                                .getRequiredVehicleType(orderWeight);

                // Filter available drivers with matching vehicle type
                List<Driver> availableDrivers = drivers.stream().filter(driver -> driver
                                .getStatus() == DriverStatus.READY_TO_ACCEPT_ORDERS
                                && driver.getVehicleType() == requiredVehicleType)
                                .limit(driverNumber).collect(Collectors.toList());
                // Filter warehouses with matching vehicle type
                List<Warehouse> matchingWarehouses = warehouseRepository
                                .findAllById(delivery.getWarehouseList()).stream()
                                .collect(Collectors.toList());

                // Ensure we have enough warehouses
                if (matchingWarehouses.isEmpty()) {
                        return response;
                }

                List<OrderDetail> orderDetails = delivery.getOrder().getOrderDetails();

                System.out.println("Start solving VRP");

                VRPSolver solver = new VRPSolver(
                                new VRPSolution(matchingWarehouses, orderDetails,
                                                availableDrivers, driverNumber));
                VRPSolution result = solver.solve();
                System.out.println("End solving VRP");

                for (WarehouseDriver warehouseDriver : result.getWarehouseDrivers()) {
                        response.addDriver(DriverDTO.fromEntity(warehouseDriver.getDriver()),
                                        warehouseDriver.getWarehouseIds());
                }

                return response;
        }
}