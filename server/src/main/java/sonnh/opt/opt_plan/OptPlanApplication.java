package sonnh.opt.opt_plan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"main.java.sonnh.opt.opt_plan.controller", "main.java.sonnh.opt.opt_plan.service"})
public class OptPlanApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptPlanApplication.class, args);
	}

}
