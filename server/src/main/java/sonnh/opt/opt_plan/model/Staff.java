package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import sonnh.opt.opt_plan.constant.enums.Position;

import java.util.List;

@Entity
@Table(name = "staffs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@ToString.Exclude
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Position position;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	@ToString.Exclude
	private Department department;

	@OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
	@ToString.Exclude
	private List<ShiftAssignment> shiftAssignments;

	@OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
	@ToString.Exclude
	private List<TaskAssignment> taskAssignments;
}