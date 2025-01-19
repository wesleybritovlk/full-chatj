package com.github.wesleybritovlk.fullchatj.infra;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.flywaydb.core.Flyway;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class InfraModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Properties.class).toProvider(PropertiesProvider.class);
        bind(Flyway.class).toProvider(FlywayProvider.class);
        bind(EntityManagerFactory.class).toProvider(EntityManagerProvider.class);
        bind(OpenApiProvider.class).to(OpenApiProviderImpl.class);
        bind(JwtProvider.class).to(JwtProviderImpl.class);
    }

    @Provides
    Executor provideExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Provides
    EntityManager provideEntityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

}
