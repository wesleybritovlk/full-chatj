package com.github.wesleybritovlk.fullchatj.app;

import com.github.wesleybritovlk.fullchatj.app.auth.AuthModule;
import com.github.wesleybritovlk.fullchatj.infra.AppConfig;
import com.github.wesleybritovlk.fullchatj.infra.OpenApiConfig;
import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AppConfig.class);
        bind(OpenApiConfig.class);
        bind(AppController.class).to(AppControllerImpl.class);
        bind(AppRoutes.class).to(AppRoutesImpl.class);
        install(new AuthModule());
    }
}
