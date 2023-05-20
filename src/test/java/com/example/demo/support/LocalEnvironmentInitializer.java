package com.example.demo.support;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

public class LocalEnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static final GenericContainer redis = new GenericContainer("redis:6-alpine")
            .withExposedPorts(6379);
    static final KafkaContainer kafka = new KafkaContainer("6.2.1");
    static final PostgreSQLContainer postgres = new
            PostgreSQLContainer(DockerImageName.parse("postgres:14-alpine"));

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        ConfigurableEnvironment env = context.getEnvironment();
        Startables.deepStart(redis, kafka, postgres).join();
        env.getPropertySources().addFirst(new MapPropertySource("testcontainers", getProperties()));
    }
    Map<String, Object> getProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("spring.datasource.url", postgres.getJdbcUrl());
        props.put("spring.datasource.username", postgres.getUsername());
        props.put("spring.datasource.password", postgres.getPassword());
        props.put("spring.redis.host", redis.getHost());
        props.put("spring.redis.port", redis.getFirstMappedPort());
        props.put("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
        return props;
    }

}
