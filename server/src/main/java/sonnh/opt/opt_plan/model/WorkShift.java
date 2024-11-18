package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "work_shifts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkShift {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private LocalTime startTime;

	@Column(nullable = false)
	private LocalTime endTime;

	@Column(nullable = false)
	private Boolean isActive;

	@OneToMany(mappedBy = "workShift")
	private List<ShiftAssignment> shiftAssignments;
}