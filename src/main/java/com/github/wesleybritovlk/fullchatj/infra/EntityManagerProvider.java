package com.github.wesleybritovlk.fullchatj.infra;

import java.util.Map;
import java.util.Properties;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EntityManagerProvider implements Provider<EntityManagerFactory> {
    private final Properties properties;

    @Override
    public EntityManagerFactory get() {
        val configOverrides = Map.of(
                "jakarta.persistence.jdbc.url", properties.getProperty("jpa.jdbc.url"),
                "jakarta.persistence.jdbc.user", properties.getProperty("jpa.jdbc.user"),
                "jakarta.persistence.jdbc.password", properties.getProperty("jpa.jdbc.password"),
                "jakarta.persistence.jdbc.driver", properties.getProperty("jpa.jdbc.driver"),
                "jakarta.persistence.schema-generation.database.action",
                properties.getProperty("jpa.schema-generation.database.action"),
                "eclipselink.logging.level", properties.getProperty("eclipselink.logging.level"),
                "eclipselink.target-database", properties.getProperty("eclipselink.target-database"),
                "eclipselink.default-schema", properties.getProperty("eclipselink.default-schema"),
                "eclipselink.ddl-generation", properties.getProperty("eclipselink.ddl-generation"));
        val entityManagerFactory = Persistence.createEntityManagerFactory("em-persistence-unit", configOverrides);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (entityManagerFactory != null && entityManagerFactory.isOpen())
                entityManagerFactory.close();
        }));
        return entityManagerFactory;
    }
}
