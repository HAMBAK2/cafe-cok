package com.sideproject.cafe_cok;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health-check")
    @Hidden
    public String getHealthCheck() {
        return "up";
    }
}
