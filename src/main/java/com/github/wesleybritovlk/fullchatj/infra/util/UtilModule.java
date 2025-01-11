package com.github.wesleybritovlk.fullchatj.infra.util;

import com.github.wesleybritovlk.fullchatj.infra.util.entitymanager.EntityManagerModule;
import com.github.wesleybritovlk.fullchatj.infra.util.jwt.JwtModule;
import com.google.inject.AbstractModule;

public class UtilModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OpenApiProvider.class);
        install(new EntityManagerModule());
        install(new JwtModule());
    }

}
