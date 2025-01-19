package com.github.wesleybritovlk.fullchatj.infra;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.Provider;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesProvider implements Provider<Properties> {

    @Override
    public Properties get() {
        val properties = new Properties();
        val dotenv = Dotenv.configure().load();
        try (val input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null)
                throw new IOException("Unable to find application.properties");
            properties.load(input);
        } catch (IOException e) {
            log.error("Failed to load application properties: {}", e.getMessage());
            throw new RuntimeException("Failed to load application properties", e);
        }
        properties.forEach((key, value) -> {
            if (value instanceof String) {
                properties.setProperty((String) key, resolveProperty((String) value, dotenv));
            }
        });
        return properties;
    }

    private String resolveProperty(String value, Dotenv dotenv) {
        String resolvedValue = value;
        while (resolvedValue.contains("${")) {
            val start = resolvedValue.indexOf("${");
            val end = resolvedValue.indexOf("}", start);
            val key = resolvedValue.substring(start + 2, end);
            val envValue = dotenv.get(key);
            if (envValue == null)
                throw new RuntimeException("Environment variable " + key + " not found in .env file");
            resolvedValue = resolvedValue.substring(0, start) + envValue + resolvedValue.substring(end + 1);
        }
        return resolvedValue;
    }
}
