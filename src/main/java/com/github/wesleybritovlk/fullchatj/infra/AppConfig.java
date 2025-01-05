package com.github.wesleybritovlk.fullchatj.infra;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private final Properties properties;
    private final Dotenv dotenv;

    public AppConfig() {
        this.properties = new Properties();
        this.dotenv = Dotenv.configure().load();
        this.loadProperties();
        this.loadEnvVariables();
    }

    private void loadProperties() {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("Unable to find application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application properties", e);
        }
    }

    private void loadEnvVariables() {
        properties.stringPropertyNames().forEach(key -> {
            String property = properties.getProperty(key);
            if (property != null && property.startsWith("${") && property.endsWith("}")) {
                String envKey = property.substring(2, property.length() - 1);
                String envValue = dotenv.get(envKey);
                if (envValue == null) {
                    throw new RuntimeException("Environment variable " + envKey + " not found in .env file");
                }
                properties.setProperty(key, envValue);
            }
        });
    }

    public Properties properties() {
        return properties;
    }

    public Dotenv dotenv() {
        return dotenv;
    }
}
