package com.github.wesleybritovlk.fullchatj.infra.util.entitymanager;

import com.google.inject.AbstractModule;

public class EntityManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EntityManagerProvider.class).to(EntityManagerProviderImpl.class);
    }

}
