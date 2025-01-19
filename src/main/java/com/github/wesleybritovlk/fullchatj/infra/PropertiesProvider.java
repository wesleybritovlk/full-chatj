package com.github.wesleybritovlk.fullchatj.infra;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.inject.Provider;

import io.github.cdimascio.dotenv.Dotenv;

public class PropertiesProvider implements Provider<Properties> {

    @Override
    public Properties get() {
        var properties = new Properties();
        var dotenv = Dotenv.configure().load();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("Unable to find application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application properties", e);
        }
        properties.stringPropertyNames().forEach(key -> {
            var property = properties.getProperty(key);
            if (property != null && property.startsWith("${") && property.endsWith("}")) {
                var envKey = property.substring(2, property.length() - 1);
                var envValue = dotenv.get(envKey);
                if (envValue == null) {
                    throw new RuntimeException("Environment variable " + envKey + " not found in .env file");
                }
                properties.setProperty(key, envValue);
            }
        });
        return properties;
    }
}
