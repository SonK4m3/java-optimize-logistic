package sonnh.opt.opt_plan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import domain.VRPSolution;
import sonnh.opt.opt_plan.payload.ApiResponse;
import sonnh.opt.opt_plan.payload.dto.VRPTaskDTO;
import sonnh.opt.opt_plan.payload.dto.VRPResultDTO;
import sonnh.opt.opt_plan.payload.request.VRPRequest;
import sonnh.opt.opt_plan.payload.request.VRPSimpleRequest;
import sonnh.opt.opt_plan.service.VRPService;
import sonnh.opt.opt_plan.constant.common.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(Api.VRP_ROUTE)
@Tag(name = "VRP Operations", description = "APIs for Vehicle Routing Problem operations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class VRPController {
    private final Logger log = LoggerFactory.getLogger(VRPController.class);
    private final VRPService vrpService;

    @Operation(summary = "Create new VRP optimization task")
    @PostMapping("/tasks")
    public ResponseEntity<ApiResponse<VRPTaskDTO>> createTask(
            @Valid @RequestBody VRPRequest request) {
        VRPTaskDTO task = vrpService.createTask(request);
        return ResponseEntity.ok(
                ApiResponse.success("VRP optimization task created successfully", task));
    }

    @Operation(summary = "Get task status and progress")
    @GetMapping("/tasks/{taskId}/status")
    public ResponseEntity<ApiResponse<VRPTaskDTO>> getTaskStatus(
            @Parameter(description = "Task ID", required = true) @PathVariable String taskId) {
        VRPTaskDTO task = vrpService.getTaskStatus(taskId);
        return ResponseEntity.ok(ApiResponse.success(task));
    }

    @Operation(summary = "Get optimization results")
    @GetMapping("/tasks/{taskId}/result")
    public ResponseEntity<ApiResponse<VRPResultDTO>> getTaskResult(
            @Parameter(description = "Task ID", required = true) @PathVariable String taskId) {
        VRPResultDTO result = vrpService.getTaskResult(taskId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "Cancel running task")
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse<String>> cancelTask(
            @Parameter(description = "Task ID", required = true) @PathVariable String taskId) {
        vrpService.cancelTask(taskId);
        return ResponseEntity.ok(ApiResponse.success("Task cancelled successfully"));
    }

    @Operation(summary = "Solve VRP problem simple")
    @PostMapping("/solve")
    public ResponseEntity<ApiResponse<VRPSolution>> solveVRP(
            @Valid @RequestBody VRPSimpleRequest request) {
        try {
            // Add logging to debug request
            log.debug("Received request: {}", request);

            VRPSolution solution = vrpService.solveVRP(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Success", solution));
        } catch (Exception e) {
            log.error("Error processing VRP request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false,
                            "Error processing request: " + e.getMessage(), null));
        }
    }
}
