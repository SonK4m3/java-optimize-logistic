
package sonnh.opt.opt_plan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import domain.solver.VRPSolver;

@Configuration
public class VRPConfig {

    @Bean
    public VRPSolver vrpSolver() { return new VRPSolver(100); }
}
