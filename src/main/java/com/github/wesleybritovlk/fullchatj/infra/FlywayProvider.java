package com.github.wesleybritovlk.fullchatj.infra;

import java.util.Properties;

import org.flywaydb.core.Flyway;

import com.google.inject.Inject;
import com.google.inject.Provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FlywayProvider implements Provider<Flyway> {
    private final Properties properties;

    private boolean bool(String property) {
        return Boolean.parseBoolean(property);
    }

    @Override
    public Flyway get() {
        return Flyway.configure()
                .dataSource(properties.getProperty("jakarta.persistence.jdbc.url"),
                        properties.getProperty("jakarta.persistence.jdbc.user"),
                        properties.getProperty("jakarta.persistence.jdbc.password"))
                .schemas(properties.getProperty("flyway.schemas").split(","))
                .defaultSchema(properties.getProperty("flyway.default-schema"))
                .locations(properties.getProperty("flyway.locations").split(","))
                .validateOnMigrate(bool(properties.getProperty("flyway.validate-on-migrate")))
                .baselineOnMigrate(bool(properties.getProperty("flyway.baseline-on-migrate")))
                .load();
    }

}
