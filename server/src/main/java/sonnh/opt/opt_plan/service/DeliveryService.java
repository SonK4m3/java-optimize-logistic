package sonnh.opt.opt_plan.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.Driver;
import sonnh.opt.opt_plan.payload.request.DeliveryUpdateRequest;
import sonnh.opt.opt_plan.payload.response.DeliveryMetrics;
import sonnh.opt.opt_plan.payload.response.SuggestDriversResponse;

@Service
@Transactional
public interface DeliveryService {
	Delivery getDeliveriesByOrderId(Long orderId);

	Delivery updateDeliveryStatus(Long deliveryId, DeliveryUpdateRequest request);

	Delivery assignDriver(Long deliveryId, Long driverId);

	List<Driver> getAvailableDrivers();

	DeliveryMetrics getDeliveryMetrics(Long deliveryId);

	void updateDriverLocation(Long driverId, Double latitude, Double longitude);

	List<Delivery> optimizeRoutes(List<Long> deliveryIds);

	List<Driver> getAvailableDriversForDelivery(Long deliveryId);

	SuggestDriversResponse suggestDriversForDelivery(Long deliveryId, Long driverNumber,
			List<Long> driverIds);
}