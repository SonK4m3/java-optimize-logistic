package sonnh.opt.opt_plan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableAsync
@ComponentScan(basePackages = {
		"sonnh.opt.opt_plan.controller", "sonnh.opt.opt_plan.service",
		"sonnh.opt.opt_plan.model", "sonnh.opt.opt_plan.repository",
		"sonnh.opt.opt_plan.config", "sonnh.opt.opt_plan.exception",
		"sonnh.opt.opt_plan.payload", "sonnh.opt.opt_plan.utils",
		"sonnh.opt.opt_plan.constant", "sonnh.opt.opt_plan.mapper", "domain.solver",
		"sonnh.opt.opt_plan.security"
})
public class OptPlanApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptPlanApplication.class, args);
	}

}
