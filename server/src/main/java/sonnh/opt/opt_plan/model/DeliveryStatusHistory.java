package sonnh.opt.opt_plan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sonnh.opt.opt_plan.constant.enums.DeliveryStatus;

import java.time.LocalDateTime;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusHistory {
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(length = 500)
    private String note;
    
    @Column(length = 50)
    private String updatedBy;
    
    @Column(nullable = false)
    private String location;
    
    // Helper method to create a status change entry
    public static DeliveryStatusHistory of(DeliveryStatus status, String note, String location) {
        return DeliveryStatusHistory.builder()
                .status(status)
                .timestamp(LocalDateTime.now())
                .note(note)
                .location(location)
                .build();
    }
} 