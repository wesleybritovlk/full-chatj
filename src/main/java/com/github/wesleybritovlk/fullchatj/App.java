package com.github.wesleybritovlk.fullchatj;

import com.github.wesleybritovlk.fullchatj.app.AppModule;
import com.github.wesleybritovlk.fullchatj.app.AppRoutes;
import com.github.wesleybritovlk.fullchatj.infra.OpenApiProvider;
import com.google.inject.Guice;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        var injector = Guice.createInjector(new AppModule());
        var app = Javalin.create(config -> {
            injector.getInstance(OpenApiProvider.class).setup(config);
        }).start();
        injector.getInstance(AppRoutes.class).setup(app);
    }
}