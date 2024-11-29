package sonnh.opt.opt_plan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.model.DeliveryFee;
import sonnh.opt.opt_plan.repository.DeliveryFeeRepository;

@Service
@RequiredArgsConstructor
public class DeliveryFeeService {
	private final DeliveryFeeRepository deliveryFeeRepository;

	public DeliveryFee calculateAndSaveDeliveryFee(Delivery delivery) {
		DeliveryFee deliveryFee = delivery.calculateDeliveryFee();
		return deliveryFeeRepository.save(deliveryFee);
	}

	public DeliveryFee getDeliveryFee(Long deliveryId) {
		return deliveryFeeRepository.findByDeliveryId(deliveryId)
				.orElseThrow(() -> new RuntimeException("Delivery fee not found"));
	}
}