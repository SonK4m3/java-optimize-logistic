package sonnh.opt.opt_plan.service;

import org.springframework.stereotype.Service;

import domain.VRPSolution;
import jakarta.validation.Valid;
import sonnh.opt.opt_plan.payload.dto.VRPResultDTO;
import sonnh.opt.opt_plan.payload.dto.VRPTaskDTO;
import sonnh.opt.opt_plan.payload.request.VRPRequest;
import sonnh.opt.opt_plan.payload.request.VRPSimpleRequest;

@Service
public interface VRPService {

    VRPTaskDTO createTask(@Valid VRPRequest request);

    VRPTaskDTO getTaskStatus(String taskId);

    VRPResultDTO getTaskResult(String taskId);

    void cancelTask(String taskId);

    VRPSolution solveVRP(VRPSimpleRequest request);
}
