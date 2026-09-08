package com.volodya.infrastructure.driving.actuator.factories;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.volodya.application.configurationproperties.HeapDumpConfigurationProperties;
import com.volodya.infrastructure.driving.actuator.entities.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static com.volodya.utils.MapUtil.orderedMap;
import static java.util.Map.entry;

@RequiredArgsConstructor
@Slf4j
public final class DevStageActuatorRecordsFactory implements ActuatorRecordsFactory {

    private final static String heapDumpMxBeanName = "com.sun.management:type=HotSpotDiagnostic";

    private final HeapDumpConfigurationProperties heapDumpConfigurationProperties;
    private final DataSource dataSource;
    private final ApplicationContext applicationContext;
    private HotSpotDiagnosticMXBean heapDumpMxBean = null;

    @Override
    public Optional<HealthResponse> createHealthResponse() {
        long startTime = System.currentTimeMillis();

        try (Connection connection = dataSource.getConnection()) {
            var metadata = connection.getMetaData();
            long latency = System.currentTimeMillis() - startTime;
            Map<String, Object> details = orderedMap(
                entry("database", metadata.getDatabaseProductName()),
                entry("version", metadata.getDatabaseProductVersion()),
                entry("driverVersion", metadata.getDriverVersion()),
                entry("url", metadata.getURL()),
                entry("mode", metadata.getURL().contains(":mem:") ? "IN-MEMORY" : "FILE-BASED"),
                entry("latencyMs", latency)
            );
            return Optional.of(HealthResponse.up(details));
        } catch (SQLException ex) {
            return Optional.of(HealthResponse.down(
                Map.of("error", ex.getMessage())
            ));
        }
    }

    @Override
    public Optional<MetricsResponse> createMetricsResponse() {
        var runtime = Runtime.getRuntime();
        var gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        var gcList = new ArrayList<Map<String, Object>>(gcBeans.size());
        for (var gcBean : gcBeans) {
            gcList.add(orderedMap(
                entry("name", gcBean.getName()),
                entry("collectionsCount", gcBean.getCollectionCount()),
                entry("collectionTimeMs", gcBean.getCollectionTime())
            ));
        }
        var threadsBean = ManagementFactory.getThreadMXBean();
        var threadsMap = orderedMap(
            entry("peakThreadCount", threadsBean.getPeakThreadCount()),
            entry("activeThreadCount", threadsBean.getThreadCount()),
            entry("availableThreadCount", runtime.availableProcessors()),
            entry("daemonThreadCount", threadsBean.getDaemonThreadCount())
        );
        return Optional.of(new MetricsResponse(
            orderedMap(
                entry("threads", threadsMap),
                entry("memory", orderedMap(
                    entry("totalMemory", runtime.totalMemory()),
                    entry("freeMemory", runtime.freeMemory()),
                    entry("maxMemory", runtime.maxMemory())
                )),
                entry("garbageCollectors", gcList)
            )
        ));
    }

    @Override
    public Optional<BeansResponse> createBeansResponse() {
        return Optional.of(new BeansResponse(
            Arrays.stream(applicationContext.getBeanDefinitionNames()).toList()
        ));
    }

    @Override
    public Optional<EnvResponse> createEnvResponse() {
        return Optional.of(
            new EnvResponse(System.getenv())
        );
    }

    @Override
    public Optional<HeapDumpResponse> createHeapDumpResponse() {
        var heapBean = getHeapBean();
        try {
            Path path = Paths.get(heapDumpConfigurationProperties.filePath());
            Path parentDir = path.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            Files.deleteIfExists(path);
            heapBean.dumpHeap(
                path.toString(),
                heapDumpConfigurationProperties.liveOnly()
            );
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return Optional.of(new HeapDumpResponse(
            new File(heapDumpConfigurationProperties.filePath())
        ));
    }

    private HotSpotDiagnosticMXBean getHeapBean() {
        if (heapDumpMxBean == null) {
            try {
                var heapDumpMxBeanServer = ManagementFactory.getPlatformMBeanServer();
                heapDumpMxBean = ManagementFactory.newPlatformMXBeanProxy(
                    heapDumpMxBeanServer,
                    heapDumpMxBeanName,
                    HotSpotDiagnosticMXBean.class
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return heapDumpMxBean;
    }
}
