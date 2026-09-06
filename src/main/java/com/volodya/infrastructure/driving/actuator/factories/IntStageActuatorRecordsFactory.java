package com.volodya.infrastructure.driving.actuator.factories;

import com.volodya.infrastructure.driving.actuator.entities.HealthResponse;
import com.volodya.infrastructure.driving.actuator.entities.InfoResponse;
import com.volodya.infrastructure.driving.actuator.entities.MetricsResponse;
import com.volodya.infrastructure.driving.actuator.entities.PrometheusResponse;

import java.util.Optional;

public final class IntStageActuatorRecordsFactory implements ActuatorRecordsFactory {

    @Override
    public Optional<HealthResponse> createHealthResponse() {
        return Optional.empty();
    }

    @Override
    public Optional<MetricsResponse> createMetricsResponse() {
        return Optional.empty();
    }

    @Override
    public Optional<InfoResponse> createInfoResponse() {
        return Optional.empty();
    }

    @Override
    public Optional<PrometheusResponse> createPrometheusResponse() {
        return Optional.empty();
    }
}
