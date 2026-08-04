package com.volodya.infrastructure.driving.controllers;

import com.volodya.infrastructure.driving.dto.responses.HealthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public final class HealthController {

    @Value("${application.name}")
    private String applicationName;

    @GetMapping
    public HealthResponse health() {
        return new HealthResponse(
            applicationName,
            "ok"
        );
    }
}
