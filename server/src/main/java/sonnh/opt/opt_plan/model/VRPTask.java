package sonnh.opt.opt_plan.model;

import jakarta.persistence.*;
import lombok.*;
import sonnh.opt.opt_plan.constant.enums.TaskStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "vrp_tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VRPTask {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String taskId;

	@Enumerated(EnumType.STRING)
	private TaskStatus status;

	@Column(columnDefinition = "TEXT")
	private String inputData;

	@Column(columnDefinition = "TEXT")
	private String result;

	private String errorMessage;

	private LocalDateTime createdAt;
	private LocalDateTime startedAt;
	private LocalDateTime completedAt;
	private Double progress;
}