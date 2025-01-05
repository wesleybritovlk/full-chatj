package com.github.wesleybritovlk.fullchatj.app;

import com.github.wesleybritovlk.fullchatj.app.auth.AuthController;
import com.google.inject.Inject;

import io.javalin.Javalin;
import lombok.RequiredArgsConstructor;

public interface AppRoutes {
    void setup(Javalin app);
}

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class AppRoutesImpl implements AppRoutes {
    private final AppController controller;
    private final AuthController authController;

    private void appController(Javalin app) {
        app.get("/", controller::get);
    }

    private void authController(Javalin app) {
        app.post("/api/v1/auth/login", authController::login);
        app.post("/api/v1/auth/register", authController::register);
        app.get("/api/v1/auth/refresh_token", authController::refreshToken);
    }

    public void setup(Javalin app) {
        this.appController(app);
        this.authController(app);
    }

}
