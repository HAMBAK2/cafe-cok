package com.sideproject.hororok.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectionTestApi {

    @GetMapping("/api/test")
    public String connectionTest() {
        return "connection ok";
    }
}
