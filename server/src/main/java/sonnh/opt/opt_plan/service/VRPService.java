package sonnh.opt.opt_plan.service;

import org.springframework.stereotype.Service;
import domain.*;
import domain.seed.VRPSeeding;
import domain.solver.VRPSolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

@Service
public class VRPService {

    private final VRPSolver vrpSolver;
    private final ObjectMapper objectMapper;

    public VRPService() {
        this.vrpSolver = new VRPSolver(1000); // TABU_LIST_SIZE = 1000
        this.objectMapper = new ObjectMapper();
    }

    public VRPSolution solveVRP(VRPSolution initialSolution) {
        return vrpSolver.solve(initialSolution);
    }

    public VRPSolution createInitialSolution(VRPSolution rawSolution) {
        return vrpSolver.initialSolution(rawSolution);
    }

    public String solveAndDisplayVRP() {
        List<Depot> depotList = VRPSeeding.createDepots();
        List<Customer> customerList = VRPSeeding.createCustomers();
        List<Vehicle> vehicleList = VRPSeeding.createVehicles();

        VRPSolution initialSolution = vrpSolver.initialSolution(new VRPSolution(depotList, customerList, vehicleList));
        VRPSolution solution = vrpSolver.solve(initialSolution);

        ObjectNode resultJson = objectMapper.createObjectNode();
        resultJson.put("initialSolution", initialSolution.toString());
        resultJson.put("optimalSolution", solution.toString());

        try {
            return objectMapper.writeValueAsString(resultJson);
        } catch (Exception e) {
            return "{\"error\": \"Failed to generate JSON\"}";
        }
    }
}
