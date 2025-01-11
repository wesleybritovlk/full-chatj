package com.github.wesleybritovlk.fullchatj.infra.util;

import com.google.inject.AbstractModule;

public class UtilModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OpenApiProvider.class).to(OpenApiProviderImpl.class);
        bind(JwtProvider.class).to(JwtProviderImpl.class);
        bind(EntityManagerProvider.class).to(EntityManagerProviderImpl.class);
    }

}
