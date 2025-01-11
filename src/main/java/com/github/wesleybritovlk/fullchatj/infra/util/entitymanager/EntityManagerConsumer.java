package com.github.wesleybritovlk.fullchatj.infra.util.entitymanager;

import jakarta.persistence.EntityManager;

@FunctionalInterface
public interface EntityManagerConsumer {
    void accept(EntityManager entityManager);
}