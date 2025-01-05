package com.github.wesleybritovlk.fullchatj.app;

import com.google.inject.Inject;

import io.javalin.Javalin;
import lombok.RequiredArgsConstructor;

public interface AppRoutes {
    void setup(Javalin app);
}

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class AppRoutesImpl implements AppRoutes {
    private final AppController controller;

    private void appController(Javalin app) {
        app.get("/", controller::get);
    }

    public void setup(Javalin app) {
        appController(app);
    }

}
