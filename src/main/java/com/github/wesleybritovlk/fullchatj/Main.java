package com.github.wesleybritovlk.fullchatj;

import com.github.wesleybritovlk.fullchatj.app.AppRoutes;
import com.github.wesleybritovlk.fullchatj.infra.OpenApiConfig;

import io.javalin.Javalin;


public class Main {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            new OpenApiConfig().configure(config);
        }).start();
        new AppRoutes().register(app);
    }
}