package com.github.wesleybritovlk.fullchatj.infra.util;

import com.github.wesleybritovlk.fullchatj.infra.util.jwt.JwtModule;
import com.google.inject.AbstractModule;

public class UtilModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OpenApiProvide.class);
        install(new JwtModule());
    }

}
