package com.github.wesleybritovlk.fullchatj.app.auth;

import com.google.inject.AbstractModule;

public class AuthModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AuthRepository.class).to(AuthRepositoryImpl.class);
        bind(AuthService.class).to(AuthServiceImpl.class);
        bind(AuthController.class).to(AuthControllerImpl.class);
    }

}
