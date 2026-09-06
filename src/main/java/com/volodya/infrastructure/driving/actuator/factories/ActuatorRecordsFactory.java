package com.volodya.infrastructure.driving.actuator.factories;

import com.volodya.infrastructure.driving.actuator.entities.*;

import java.util.Optional;

public interface ActuatorRecordsFactory {

    default Optional<HealthResponse> createHealthResponse() {
        return Optional.empty();
    }

    default Optional<MetricsResponse> createMetricsResponse() {
        return Optional.empty();
    }

    default Optional<BeansResponse> createBeansResponse() {
        return Optional.empty();
    }

    default Optional<EnvResponse> createEnvResponse() {
        return Optional.empty();
    }

    default Optional<HeapDumpResponse> createHeapDumpResponse() {
        return Optional.empty();
    }

    default Optional<InfoResponse> createInfoResponse() {
        return Optional.empty();
    }

    default Optional<PrometheusResponse> createPrometheusResponse() {
        return Optional.empty();
    }
}
