package com.github.wesleybritovlk.fullchatj;

import com.github.wesleybritovlk.fullchatj.app.AppModule;
import com.github.wesleybritovlk.fullchatj.app.AppRoutes;
import com.github.wesleybritovlk.fullchatj.infra.OpenApiConfig;
import com.google.inject.Guice;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        var injector = Guice.createInjector(new AppModule());
        var app = Javalin.create(config -> {
            injector.getInstance(OpenApiConfig.class).configure(config);
        }).start();
        injector.getInstance(AppRoutes.class).setup(app);
    }
}