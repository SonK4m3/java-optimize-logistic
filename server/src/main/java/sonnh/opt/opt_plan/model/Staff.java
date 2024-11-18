package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sonnh.opt.opt_plan.constant.enums.Position;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "staffs")
@NoArgsConstructor
@AllArgsConstructor
public class Staff extends User {
	@Column(nullable = false)
	private String code;

	@Column(nullable = false)
	private String fullName;

	@Column(nullable = false)
	private Boolean isActive;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Position position; // MANAGER, SUPERVISOR, WORKER

	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;

	@OneToMany(mappedBy = "staff")
	private List<ShiftAssignment> shiftAssignments;

	@OneToMany(mappedBy = "staff")
	private List<TaskAssignment> taskAssignments;

	@ManyToMany
	@JoinTable(name = "staff_roles", joinColumns = @JoinColumn(name = "staff_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
}