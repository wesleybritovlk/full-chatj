package com.github.wesleybritovlk.fullchatj.infra;

import java.util.Properties;

import org.flywaydb.core.Flyway;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EntityManagerProvider implements Provider<EntityManagerFactory> {

    private final Properties properties;
    private final Flyway flyway;

    @Override
    public EntityManagerFactory get() {
        flyway.migrate();
        val persistenceUnitName = "fullchatj-persistence-unit";
        val entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
                log.info("Closing EntityManagerFactory...");
                entityManagerFactory.close();
                log.info("EntityManagerFactory closed.");
            }
        }));
        return entityManagerFactory;
    }
}
