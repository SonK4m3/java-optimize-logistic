
package sonnh.opt.opt_plan.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Customer;
import domain.Depot;
import domain.Location;
import domain.Vehicle;
import domain.VRPSolution;
import domain.solver.VRPSolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sonnh.opt.opt_plan.constant.enums.TaskStatus;
import sonnh.opt.opt_plan.exception.ResourceNotFoundException;
import sonnh.opt.opt_plan.model.VRPTask;
import sonnh.opt.opt_plan.payload.dto.VRPResultDTO;
import sonnh.opt.opt_plan.payload.dto.VRPTaskDTO;
import sonnh.opt.opt_plan.payload.dto.VehicleDTO;
import sonnh.opt.opt_plan.payload.request.VRPRequest;
import sonnh.opt.opt_plan.payload.request.VRPSimpleRequest;
import sonnh.opt.opt_plan.repository.VRPTaskRepository;
import sonnh.opt.opt_plan.service.VRPService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VRPServiceImpl implements VRPService {
	private final VRPTaskRepository taskRepository;
	private final ObjectMapper objectMapper;

	@Override
	@Transactional
	public VRPTaskDTO createTask(VRPRequest request) {
		validateRequest(request);

		String taskId = generateTaskId();

		VRPTask task = VRPTask.builder().taskId(taskId).status(TaskStatus.CREATED)
				.inputData(serializeRequest(request)).createdAt(LocalDateTime.now())
				.progress(0.0).build();

		task = taskRepository.save(task);

		// Start async solving
		solveVRPAsync(task.getId(), request);

		return convertToTaskDTO(task);
	}

	@Override
	@Transactional(readOnly = true)
	public VRPTaskDTO getTaskStatus(String taskId) {
		VRPTask task = findTaskByIdOrThrow(taskId);
		return convertToTaskDTO(task);
	}

	@Override
	@Transactional(readOnly = true)
	public VRPResultDTO getTaskResult(String taskId) {
		VRPTask task = findTaskByIdOrThrow(taskId);

		if (task.getStatus() == TaskStatus.FAILED) {
			throw new IllegalStateException("Task failed: " + task.getErrorMessage());
		}

		if (task.getStatus() != TaskStatus.COMPLETED) {
			throw new IllegalStateException(
					"Task is not completed yet. Current status: " + task.getStatus());
		}

		return deserializeResult(task.getResult());
	}

	@Override
	@Transactional
	public void cancelTask(String taskId) {
		VRPTask task = findTaskByIdOrThrow(taskId);

		if (task.getStatus() == TaskStatus.COMPLETED
				|| task.getStatus() == TaskStatus.FAILED) {
			throw new IllegalStateException("Cannot cancel completed or failed task");
		}

		task.setStatus(TaskStatus.CANCELLED);
		task.setCompletedAt(LocalDateTime.now());
		taskRepository.save(task);
	}

	@Async
	protected void solveVRPAsync(Long taskId, VRPRequest request) {
		VRPTask task = taskRepository.findById(taskId)
				.orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

		try {
			updateTaskStatus(task, TaskStatus.PROCESSING);

			// Convert request data to domain model
			java.util.List<Depot> depots = convertToDepots(request.getDepot());
			java.util.List<Customer> customers = convertToCustomers(request.getOrders());
			java.util.List<Vehicle> vehicles = convertToVehicles(request.getVehicles());

			// Create VRP solver with tabu list size
			VRPSolver solver = new VRPSolver(1000);

			// Create initial solution using greedy algorithm
			VRPSolution initialSolution = solver
					.initialSolution(new VRPSolution(depots, customers, vehicles));
			updateProgress(task, 30.0);

			// Solve using Tabu Search algorithm
			VRPSolution result = solver.solve(initialSolution);
			updateProgress(task, 80.0);

			// Convert solution to DTO and save
			VRPResultDTO resultDTO = convertSolutionToDTO(result);
			task.setStatus(TaskStatus.COMPLETED);
			task.setResult(serializeResult(resultDTO));
			task.setProgress(100.0);
			task.setCompletedAt(LocalDateTime.now());

		} catch (Exception e) {
			log.error("Error solving VRP task {}: {}", task.getTaskId(), e.getMessage());
			task.setStatus(TaskStatus.FAILED);
			task.setErrorMessage(e.getMessage());
			task.setCompletedAt(LocalDateTime.now());
		}

		taskRepository.save(task);
	}

	private void validateRequest(VRPRequest request) {
		if (request.getOrders() == null || request.getOrders().isEmpty()) {
			throw new IllegalArgumentException("Orders list cannot be empty");
		}
		if (request.getVehicles() == null || request.getVehicles().isEmpty()) {
			throw new IllegalArgumentException("Vehicles list cannot be empty");
		}
		if (request.getDepot() == null) {
			throw new IllegalArgumentException("Depot location is required");
		}
	}

	private String generateTaskId() {
		return "VRP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}

	private VRPTask findTaskByIdOrThrow(String taskId) {
		return taskRepository.findByTaskId(taskId)
				.orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
	}

	private void updateTaskStatus(VRPTask task, TaskStatus status) {
		task.setStatus(status);
		if (status == TaskStatus.PROCESSING) {
			task.setStartedAt(LocalDateTime.now());
		}
		taskRepository.save(task);
	}

	private String serializeRequest(VRPRequest request) {
		try {
			return objectMapper.writeValueAsString(request);
		} catch (Exception e) {
			throw new RuntimeException("Error serializing request", e);
		}
	}

	private String serializeResult(VRPResultDTO result) {
		try {
			return objectMapper.writeValueAsString(result);
		} catch (Exception e) {
			throw new RuntimeException("Error serializing result", e);
		}
	}

	private VRPResultDTO deserializeResult(String json) {
		try {
			return objectMapper.readValue(json, VRPResultDTO.class);
		} catch (Exception e) {
			throw new RuntimeException("Error deserializing result", e);
		}
	}

	private VRPTaskDTO convertToTaskDTO(VRPTask task) {
		return VRPTaskDTO.builder().taskId(task.getTaskId()).status(task.getStatus())
				.createdAt(task.getCreatedAt()).startedAt(task.getStartedAt())
				.completedAt(task.getCompletedAt()).progress(task.getProgress())
				.errorMessage(task.getErrorMessage()).build();
	}

	private List<Depot> convertToDepots(VRPRequest.Location depotLocation) {
		Location location = new Location(1, depotLocation.getLatitude(),
				depotLocation.getLongitude());
		Depot depot = new Depot(1L, location);
		return List.of(depot);
	}

	private List<Customer> convertToCustomers(List<VRPRequest.Order> orders) {
		return orders.stream().map(order -> {
			Location location = new Location(1, order.getLocation().getLatitude(),
					order.getLocation().getLongitude());
			return new Customer(order.getId(), location, order.getDemand().intValue());
		}).collect(Collectors.toList());
	}

	private List<Vehicle> convertToVehicles(List<VRPRequest.Vehicle> vehicles) {
		return vehicles.stream().map(vehicle -> new Vehicle((long) vehicle.getId(),
				vehicle.getCapacity().intValue())).collect(Collectors.toList());
	}

	private VRPResultDTO convertSolutionToDTO(VRPSolution result) {
		return VRPResultDTO.builder()
				.vehicles(result.getVehicleList().stream()
						.map(vehicle -> VehicleDTO.builder().id(vehicle.getId())
								.capacity(Double.valueOf(vehicle.getCapacity())).build())
						.collect(Collectors.toList()))
				.totalDistance(result.calculateScore()).totalTime(0.0)
				.totalOrders(result.getCustomerList().size()).totalCost(0.0).build();
	}

	private void updateProgress(VRPTask task, Double progress) {
		task.setProgress(progress);
		taskRepository.save(task);
		log.debug("Updated task {} progress to {}%", task.getTaskId(), progress);
	}

	@Override
	public VRPSolution solveVRP(VRPSimpleRequest request) {
		List<Depot> depots = request.getDepots().stream()
				.map(depot -> new Depot(depot.getId(),
						new Location(depot.getLocation().getId(),
								depot.getLocation().getX(), depot.getLocation().getY())))
				.collect(Collectors.toList());
		List<Customer> customers = request.getCustomers().stream()
				.map(customer -> new Customer(customer.getId(),
						new Location(customer.getLocation().getId(),
								customer.getLocation().getX(),
								customer.getLocation().getY()),
						customer.getDemand()))
				.collect(Collectors.toList());
		List<Vehicle> vehicles = request.getVehicles().stream()
				.map(vehicle -> new Vehicle(vehicle.getId(), vehicle.getCapacity()))
				.collect(Collectors.toList());

		VRPSolver solver = new VRPSolver(1000);
		VRPSolution initialSolution = solver
				.initialSolution(new VRPSolution(depots, customers, vehicles));
		VRPSolution solution = solver.solve(initialSolution);
		return solution;
	}
}