package com.volodya.application.configuration;

import com.volodya.application.configurationproperties.DatabaseConfigurationProperties;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.volodya.infrastructure.driven.repositories")
public abstract class PersistenceConfig {

    private final static String H2_EMBED_TYPE = "h2-embed";

    @Bean
    public DataSource dataSource(
        DatabaseConfigurationProperties config
    ) throws ClassNotFoundException {
        if (Objects.equals(config.dbType(), H2_EMBED_TYPE)) {
            var builder = new EmbeddedDatabaseBuilder();
            return builder.setType(EmbeddedDatabaseType.H2).build();
        } else {
            Class<? extends Driver> driverClass = Class.forName(
                config.dbDriverClassName(),
                true,
                PersistenceConfig.class.getClassLoader()
            ).asSubclass(Driver.class);

            var dataSource = new SimpleDriverDataSource();
            dataSource.setDriverClass(driverClass);
            dataSource.setUrl("jdbc:" + config.dbType() + "://" + config.dbHost() + ':' + config.dbPort() + "/" + config.dbName());
            dataSource.setUsername(config.dbUsername());
            dataSource.setPassword(config.dbPassword());
            var hikariDataSource = new HikariDataSource();
            hikariDataSource.setDataSource(dataSource);
            return hikariDataSource;
        }
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure(PersistenceConfig.class.getClassLoader())
            .dataSource(dataSource)
            .locations("classpath:db/migration")
            .load();
    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        var vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);

        var factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.volodya.infrastructure.driven.entities");
        factory.setDataSource(dataSource);

        var jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "validate");
        jpaProperties.put(
            "hibernate.physical_naming_strategy",
            "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
        );
        factory.setJpaProperties(jpaProperties);
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
