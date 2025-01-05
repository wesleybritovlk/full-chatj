package com.github.wesleybritovlk.fullchatj.infra;

import java.io.IOException;
import java.util.Properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppConfig {
    private Properties loadProperties() {
        var properties = new Properties();
        try (var inputStream = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public String getProperty(String propertyName) {
        return loadProperties().getProperty(propertyName);
    }
}
