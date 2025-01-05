package com.github.wesleybritovlk.fullchatj;

import com.github.wesleybritovlk.fullchatj.app.AppModule;
import com.github.wesleybritovlk.fullchatj.app.AppRoutes;
import com.github.wesleybritovlk.fullchatj.infra.OpenApiConfig;
import com.google.inject.Guice;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            new OpenApiConfig().configure(config);
        }).start();
        var injector = Guice.createInjector(new AppModule());
        var routes = injector.getInstance(AppRoutes.class);
        routes.setup(app);
    }
}