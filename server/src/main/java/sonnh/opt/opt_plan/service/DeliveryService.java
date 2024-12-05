package sonnh.opt.opt_plan.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.payload.response.DeliveryMetrics;
import sonnh.opt.opt_plan.payload.response.SuggestDriversResponse;

@Service
@Transactional
public interface DeliveryService {
	Delivery getDeliveryOrThrow(Long deliveryId);

	Delivery getDeliveryByOrderId(Long orderId);

	List<Delivery> optimizeRoutes(List<Long> deliveryIds);

	DeliveryMetrics getDeliveryMetrics(Long deliveryId);

	SuggestDriversResponse suggestDriversForDelivery(Long deliveryId, Long driverNumber,
			List<Long> driverIds);

	SuggestDriversResponse suggestDriversForDeliveryVRP(Long deliveryId, Long driverNumber,
			List<Long> driverIds);
}