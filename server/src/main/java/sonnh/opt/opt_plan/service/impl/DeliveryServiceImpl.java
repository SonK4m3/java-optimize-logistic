package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.Delivery;
import sonnh.opt.opt_plan.payload.request.DeliveryCreateRequest;
import sonnh.opt.opt_plan.repository.DeliveryRepository;
import sonnh.opt.opt_plan.service.DeliveryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getPendingDeliveries() {
        return deliveryRepository.findByStatus(DeliveryStatus.PENDING);
    }

    @Override
    @Transactional
    public Delivery createDelivery(DeliveryCreateRequest request) {
        Delivery delivery = Delivery.builder().status(DeliveryStatus.PENDING)
                .deliveryNote(request.getNotes())
                .createdAt(request.getRequestedDeliveryTime())
                .updatedAt(request.getRequestedDeliveryTime()).build();

        return deliveryRepository.save(delivery);
    }

    @Transactional
    public Delivery updateDeliveryStatus(Long deliveryId, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));

        delivery.setStatus(status);

        return deliveryRepository.save(delivery);
    }
}