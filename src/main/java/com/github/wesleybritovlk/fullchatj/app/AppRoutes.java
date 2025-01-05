package com.github.wesleybritovlk.fullchatj.app;

import io.javalin.Javalin;

public class AppRoutes {

    private void appController(Javalin app) {
        var controller = new AppController();
        app.get("/", controller::get);
    }

    public void register(Javalin app) {
        appController(app);
    }


}
