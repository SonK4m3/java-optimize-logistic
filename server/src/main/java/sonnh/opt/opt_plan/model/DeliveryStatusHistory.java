package sonnh.opt.opt_plan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_status_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private DeliveryStatus status;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String note;

    private String updatedBy;

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        if (delivery != null && !delivery.getStatusHistory().contains(this)) {
            delivery.getStatusHistory().add(this);
        }
    }
}