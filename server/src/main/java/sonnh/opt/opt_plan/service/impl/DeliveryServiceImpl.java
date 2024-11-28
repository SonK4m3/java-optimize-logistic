package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.model.Order;
import sonnh.opt.opt_plan.payload.request.DeliveryCreateRequest;
import sonnh.opt.opt_plan.payload.request.DeliveryUpdateRequest;
import sonnh.opt.opt_plan.repository.DeliveryRepository;
import sonnh.opt.opt_plan.repository.DriverRepository;
import sonnh.opt.opt_plan.repository.OrderRepository;
import sonnh.opt.opt_plan.service.DeliveryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final DriverRepository driverRepository;

    /**
     * Get deliveries by order ID
     * 
     * @param orderId Order ID to find deliveries
     * @return List of deliveries associated with the order
     */
    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getDeliveriesByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId));

        return deliveryRepository.findByOrder(order);
    }

    /**
     * Update delivery status
     * 
     * @param deliveryId Delivery ID to update
     * @param request    Request containing new status and note
     * @return Updated delivery
     */
    @Override
    @Transactional
    public Delivery updateDeliveryStatus(Long deliveryId, DeliveryUpdateRequest request) {
        Delivery delivery = deliveryRepository.findByIdWithHistory(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Delivery not found with id: " + deliveryId));

        delivery.updateStatus(request.getStatus(), request.getNote());
        return deliveryRepository.save(delivery);
    }

    /**
     * Assign driver to delivery
     * 
     * @param deliveryId Delivery ID to assign driver
     * @param driverId   Driver ID to be assigned
     * @return Updated delivery with assigned driver
     */
    @Override
    @Transactional
    public Delivery assignDriver(Long deliveryId, Long driverId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Delivery not found with id: " + deliveryId));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver not found with id: " + driverId));

        delivery.setDriver(driver);
        delivery.updateStatus(DeliveryStatus.IN_TRANSIT,
                "Assigned to driver: " + driver.getFullName());

        return deliveryRepository.save(delivery);
    }
}