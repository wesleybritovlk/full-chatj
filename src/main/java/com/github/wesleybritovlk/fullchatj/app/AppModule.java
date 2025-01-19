package com.github.wesleybritovlk.fullchatj.app;

import com.github.wesleybritovlk.fullchatj.app.auth.AuthModule;
import com.github.wesleybritovlk.fullchatj.infra.InfraModule;
import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AppRoutes.class).to(AppRoutesImpl.class);
        bind(AppController.class).to(AppControllerImpl.class);
        install(new InfraModule());
        install(new AuthModule());
    }

}
