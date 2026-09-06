package com.volodya.infrastructure.driving.controllers;

import com.volodya.application.exceptions.EndpointNotFoundException;
import com.volodya.infrastructure.driving.actuator.entities.BeansResponse;
import com.volodya.infrastructure.driving.actuator.entities.EnvResponse;
import com.volodya.infrastructure.driving.actuator.entities.HealthResponse;
import com.volodya.infrastructure.driving.actuator.entities.MetricsResponse;
import com.volodya.infrastructure.driving.actuator.factories.ActuatorRecordsFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/actuator")
@RequiredArgsConstructor
public final class ActuatorController {

    private final ActuatorRecordsFactory actuatorRecordsFactory;

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health(HttpServletRequest request) {
        return toResponseEntity(actuatorRecordsFactory.createHealthResponse(), request);
    }

    @GetMapping("/metrics")
    public ResponseEntity<MetricsResponse> metrics(HttpServletRequest request) {
        return toResponseEntity(actuatorRecordsFactory.createMetricsResponse(), request);
    }

    @GetMapping("/beans")
    public ResponseEntity<BeansResponse> beans(HttpServletRequest request) {
        return toResponseEntity(actuatorRecordsFactory.createBeansResponse(), request);
    }

    @GetMapping("/env")
    public ResponseEntity<EnvResponse> env(HttpServletRequest request) {
        return toResponseEntity(actuatorRecordsFactory.createEnvResponse(), request);
    }

    @GetMapping("/heapdump")
    public ResponseEntity<Resource> heapDump(HttpServletRequest request) {
        var heapDumpResponse = actuatorRecordsFactory
            .createHeapDumpResponse()
            .orElseThrow(() -> new EndpointNotFoundException(request));
        var heapDumpFile = heapDumpResponse.heapDumpFile();
        var heapDumpResource = new FileSystemResource(heapDumpFile);

        try {
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + heapDumpResource.getFilename() + "\"")
                .contentLength(heapDumpResource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(heapDumpResource);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static <T> ResponseEntity<T> toResponseEntity(
        Optional<T> result,
        HttpServletRequest request
    ) {
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            throw new EndpointNotFoundException(request);
        }
    }
}
