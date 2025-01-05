package com.github.wesleybritovlk.fullchatj.infra.util.jwt;

import com.google.inject.AbstractModule;

public class JwtModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Jwt.class);
        bind(JwtProvide.class).to(JwtProvideImpl.class);
    }

}
