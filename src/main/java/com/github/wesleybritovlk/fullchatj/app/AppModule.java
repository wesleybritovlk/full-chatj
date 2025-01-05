package com.github.wesleybritovlk.fullchatj.app;

import java.util.Properties;

import com.github.wesleybritovlk.fullchatj.app.auth.AuthModule;
import com.github.wesleybritovlk.fullchatj.infra.AppConfig;
import com.github.wesleybritovlk.fullchatj.infra.util.UtilModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import io.github.cdimascio.dotenv.Dotenv;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AppController.class).to(AppControllerImpl.class);
        bind(AppRoutes.class).to(AppRoutesImpl.class);
        install(new UtilModule());
        install(new AuthModule());
    }

    @Provides
    @Singleton
    public AppConfig provideAppConfig() {
        return new AppConfig();
    }

    @Provides
    @Singleton
    public Properties provideProperties(AppConfig appConfig) {
        return appConfig.properties();
    }

    @Provides
    @Singleton
    public Dotenv provideDotenv(AppConfig appConfig) {
        return appConfig.dotenv();
    }
}
