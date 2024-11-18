package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sonnh.opt.opt_plan.constant.enums.ShiftStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "shift_assignments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftAssignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "staff_id", nullable = false)
	private Staff staff;

	@ManyToOne
	@JoinColumn(name = "work_shift_id", nullable = false)
	private WorkShift workShift;

	@Column(nullable = false)
	private LocalDate workDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ShiftStatus status; // SCHEDULED, COMPLETED, ABSENT

	private String note;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
}