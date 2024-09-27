package main.java.sonnh.opt.opt_plan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vrp")
public class VRPController {

    @GetMapping("/solve")
    public String solve() {
        return "Hello World";
    }
}
