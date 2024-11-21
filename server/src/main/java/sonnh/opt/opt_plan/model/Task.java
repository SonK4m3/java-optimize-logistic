package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sonnh.opt.opt_plan.constant.enums.TaskPriority;
import sonnh.opt.opt_plan.constant.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	private String description;

	@Column(nullable = false)
	private LocalDateTime startTime;

	@Column(nullable = false)
	private LocalDateTime endTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TaskPriority priority; // HIGH, MEDIUM, LOW

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TaskStatus status; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED

	@OneToMany(mappedBy = "task")
	@JsonIgnore
	private List<TaskAssignment> assignments;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
}