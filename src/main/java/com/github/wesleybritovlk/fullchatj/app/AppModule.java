package com.github.wesleybritovlk.fullchatj.app;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AppController.class).to(AppControllerImpl.class);
        bind(AppRoutes.class).to(AppRoutesImpl.class);
    }
}
