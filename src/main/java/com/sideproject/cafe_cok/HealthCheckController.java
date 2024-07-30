package com.sideproject.cafe_cok;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health-check")
    public String getHealthCheck() {
        return "up";
    }
}
