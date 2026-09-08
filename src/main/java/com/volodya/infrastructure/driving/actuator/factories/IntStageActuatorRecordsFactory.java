package com.volodya.infrastructure.driving.actuator.factories;

import com.volodya.application.configurationproperties.ApplicationInfoConfigurationProperties;
import com.volodya.infrastructure.driving.actuator.entities.HealthResponse;
import com.volodya.infrastructure.driving.actuator.entities.InfoResponse;
import com.volodya.infrastructure.driving.actuator.entities.MetricsResponse;
import com.volodya.infrastructure.driving.actuator.entities.PrometheusResponse;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import static com.volodya.utils.MapUtil.orderedMap;
import static java.util.Map.entry;

public final class IntStageActuatorRecordsFactory implements ActuatorRecordsFactory {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final InfoResponse cachedInfoResponse;

    public IntStageActuatorRecordsFactory(
        DataSource dataSource,
        ApplicationInfoConfigurationProperties applicationInfo
    ) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.cachedInfoResponse = new InfoResponse(orderedMap(
            entry("applicationName", applicationInfo.name()),
            entry("applicationVersion", applicationInfo.version()),
            entry("commitHash", applicationInfo.commitHash()),
            entry("buildTimestamp", applicationInfo.buildTimestamp())
        ));
    }

    @Override
    public Optional<HealthResponse> createHealthResponse() {

        long startTime = System.currentTimeMillis();

        try (var connection = dataSource.getConnection()) {
            var metadata = connection.getMetaData();
            long latency = System.currentTimeMillis() - startTime;
            String activeTxQuery = """
                SELECT count(*) AS active_transactions
                FROM pg_stat_activity
                WHERE state IN ('active', 'idle in transaction');
                """;
            Integer activeTransactions = jdbcTemplate.queryForObject(activeTxQuery, Integer.class);
            return Optional.of(HealthResponse.up(
                orderedMap(
                    entry("database", metadata.getDatabaseProductName()),
                    entry("version", metadata.getDatabaseProductVersion()),
                    entry("latencyMs", latency),
                    entry("activeTransactions", activeTransactions != null ? activeTransactions : "unknown")
                )
            ));
        } catch (SQLException ex) {
            return Optional.of(HealthResponse.down(
                Map.of("error", ex.getMessage())
            ));
        }
    }

    @Override
    public Optional<MetricsResponse> createMetricsResponse() {
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            var poolMXBean = hikariDataSource.getHikariPoolMXBean();

            return Optional.of(new MetricsResponse(orderedMap(
                entry("connectionPool", orderedMap(
                    entry("activeConnections", poolMXBean.getActiveConnections()),
                    entry("idleConnections", poolMXBean.getIdleConnections()),
                    entry("totalConnections", poolMXBean.getTotalConnections()),
                    entry("threadsAwaitingConnection", poolMXBean.getThreadsAwaitingConnection()),
                    entry("maxPoolSize", hikariDataSource.getMaximumPoolSize())
                ))
            )));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<InfoResponse> createInfoResponse() {
        return Optional.of(cachedInfoResponse);
    }

    @Override
    public Optional<PrometheusResponse> createPrometheusResponse() {
        return Optional.empty();
    }
}
