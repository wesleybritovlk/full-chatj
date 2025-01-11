package com.github.wesleybritovlk.fullchatj.infra.util.entitymanager;

import java.util.Map;
import java.util.Properties;

import com.google.inject.Inject;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

public interface EntityManagerProvider {
    void execute(EntityManagerConsumer consumer);
}

@Slf4j
class EntityManagerProviderImpl implements EntityManagerProvider {
    private final EntityManagerFactory entityManagerFactory;

    @Inject
    public EntityManagerProviderImpl(Properties properties) {
        this.entityManagerFactory = createEntityManagerFactory(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    private EntityManagerFactory createEntityManagerFactory(Properties properties) {
        var configOverrides = Map.of(
                "jakarta.persistence.jdbc.url", properties.getProperty("jpa.jdbc.url"),
                "jakarta.persistence.jdbc.user", properties.getProperty("jpa.jdbc.user"),
                "jakarta.persistence.jdbc.password", properties.getProperty("jpa.jdbc.password"),
                "jakarta.persistence.jdbc.driver", properties.getProperty("jpa.jdbc.driver"),
                "jakarta.persistence.schema-generation.database.action",
                properties.getProperty("jpa.schema-generation.database.action"),
                "eclipselink.logging.level", properties.getProperty("eclipselink.logging.level"),
                "eclipselink.target-database", properties.getProperty("eclipselink.target-database"),
                "eclipselink.ddl-generation", properties.getProperty("eclipselink.ddl-generation"),
                "eclipselink.ddl-generation.output-mode",
                properties.getProperty("eclipselink.ddl-generation.output-mode"));
        log.info("Creating EntityManagerFactory with configOverrides: {}", configOverrides);
        try {
            var emf = Persistence.createEntityManagerFactory("em-persistence-unit", configOverrides);
            if (emf == null)
                log.error("EntityManagerFactory creation returned null");
            else
                log.info("EntityManagerFactory successfully created: {}", emf);
            return emf;
        } catch (Exception e) {
            log.error("Exception occurred while creating EntityManagerFactory", e);
            return null;
        }
    }

    private void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen())
            entityManagerFactory.close();
    }

    @Override
    public void execute(EntityManagerConsumer consumer) {
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            consumer.accept(entityManager);
        }
    }
}
