package com.github.wesleybritovlk.fullchatj.infra;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.Provider;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.val;

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
            throw new RuntimeException("Failed to load application properties", e);
        }
        properties.stringPropertyNames().forEach(key -> {
            val property = properties.getProperty(key);
            if (property != null && property.startsWith("${") && property.endsWith("}")) {
                val envKey = property.substring(2, property.length() - 1);
                val envValue = dotenv.get(envKey);
                if (envValue == null)
                    throw new RuntimeException("Environment valiable " + envKey + " not found in .env file");
                properties.setProperty(key, envValue);
            }
        });
        return properties;
    }
}
