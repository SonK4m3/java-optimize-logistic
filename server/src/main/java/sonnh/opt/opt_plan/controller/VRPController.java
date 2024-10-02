package sonnh.opt.opt_plan.controller;

import org.springframework.web.bind.annotation.*;
import domain.VRPSolution;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import sonnh.opt.opt_plan.service.VRPService;

@RestController
@RequestMapping("/api/vrp")
@Tag(name = "VRP", description = "Vehicle Routing Problem API")
public class VRPController {

    private final VRPService vrpService;

    public VRPController(VRPService vrpService) {
        this.vrpService = vrpService;
    }

    @PostMapping("/solve")
    @Operation(summary = "Solve VRP", description = "Solves the Vehicle Routing Problem given an initial solution")
    public VRPSolution solveVRP(@RequestBody VRPSolution initialSolution) {
        return vrpService.solveVRP(initialSolution);
    }

    @PostMapping("/initial-solution")
    @Operation(summary = "Create Initial Solution", description = "Creates an initial solution for the VRP")
    public VRPSolution createInitialSolution(@RequestBody VRPSolution rawSolution) {
        return vrpService.createInitialSolution(rawSolution);
    }

    @GetMapping(value = "/solve-and-display", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Solve and Display VRP", description = "Solves the VRP and returns a detailed solution")
    public ResponseEntity<String> solveAndDisplayVRP() {
        String jsonResult = vrpService.solveAndDisplayVRP();
        return ResponseEntity.ok(jsonResult);
    }
}
