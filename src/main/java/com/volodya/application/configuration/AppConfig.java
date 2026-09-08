package com.volodya.application.configuration;

import com.volodya.application.configurationproperties.ApplicationInfoConfigurationProperties;
import com.volodya.application.configurationproperties.HeapDumpConfigurationProperties;
import com.volodya.domain.events.EventListener;
import com.volodya.domain.events.EventNotifier;
import com.volodya.domain.events.EventNotifierImpl;
import com.volodya.domain.repositories.BookRepository;
import com.volodya.domain.services.BookService;
import com.volodya.domain.services.BookServiceImpl;
import com.volodya.domain.services.TransactionService;
import com.volodya.infrastructure.driving.actuator.factories.ActuatorRecordsFactory;
import com.volodya.infrastructure.driving.actuator.factories.DevStageActuatorRecordsFactory;
import com.volodya.infrastructure.driving.actuator.factories.IntStageActuatorRecordsFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@ComponentScan("com.volodya")
@EnableWebMvc
public abstract class AppConfig {

    @Value("${profiles.active}")
    private String activeProfile;

    @Bean
    public EventNotifier eventNotifier(
        List<EventListener> eventListeners
    ) {
        return new EventNotifierImpl(eventListeners);
    }

    @Bean
    public BookService bookService(
        BookRepository bookRepository,
        TransactionService transactionService,
        EventNotifier eventNotifier
    ) {
        return new BookServiceImpl(
            bookRepository,
            transactionService,
            eventNotifier
        );
    }

    @Bean
    public ActuatorRecordsFactory actuatorRecordsFactory(
        DataSource dataSource,
        ApplicationContext applicationContext
    ) {
        return switch (activeProfile) {
            case "dev" -> new DevStageActuatorRecordsFactory(
                applicationContext.getBean(HeapDumpConfigurationProperties.class),
                dataSource,
                applicationContext
            );
            case "int" -> new IntStageActuatorRecordsFactory(
                dataSource,
                applicationContext.getBean(ApplicationInfoConfigurationProperties.class)
            );
            default -> new ActuatorRecordsFactory() {};
        };
    }
}
