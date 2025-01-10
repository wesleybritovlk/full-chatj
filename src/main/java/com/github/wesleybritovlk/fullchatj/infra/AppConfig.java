package com.github.wesleybritovlk.fullchatj.infra;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.github.cdimascio.dotenv.Dotenv;

public class AppConfig {
    private final Properties properties;
    private final Dotenv dotenv;

    public AppConfig() {
        this.properties = new Properties();
        this.dotenv = Dotenv.configure().load();
        this.loadEnvProperties();
    }

    public Properties properties() {
        return properties;
    }

    public Dotenv dotenv() {
        return dotenv;
    }

    private void loadEnvProperties() {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null)
                throw new IOException("Unable to find application.properties");
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application properties", e);
        }
        properties.stringPropertyNames().forEach(key -> {
            var property = properties.getProperty(key);
            if (property != null && property.startsWith("${") && property.endsWith("}")) {
                var envKey = property.substring(2, property.length() - 1);
                var envValue = dotenv.get(envKey);
                if (envValue == null)
                    throw new RuntimeException("Environment variable " + envKey + " not found in .env file");
                properties.setProperty(key, envValue);
            }
        });
    }

}
