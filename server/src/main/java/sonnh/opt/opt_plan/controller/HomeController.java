
package sonnh.opt.opt_plan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sonnh.opt.opt_plan.constant.common.Api;

@RestController
@RequestMapping(Api.HOME_ROUTE)
public class HomeController {
    @GetMapping("/")
    public String home() { return "Welcome to the home Sonnh page"; }
}
