package com.github.wesleybritovlk.fullchatj.infra;

import com.fasterxml.jackson.databind.JsonNode;

import io.javalin.config.JavalinConfig;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;

public class OpenApiConfig {
    public void configure(JavalinConfig config) {
        var documentationPath = "/docs/api.json";
        config.registerPlugin(new OpenApiPlugin(plugin -> {
            plugin.withDocumentationPath(documentationPath);
            plugin.withDefinitionConfiguration((version, definition) -> {
                definition.withInfo(info -> {
                    info.title(AppConfig.getProperty("app.name"));
                    info.description(AppConfig.getProperty("app.description"));
                    info.version(AppConfig.getProperty("app.version"));
                    info.contact(
                            AppConfig.getProperty("app.contact.name"),
                            AppConfig.getProperty("app.contact.url"),
                            AppConfig.getProperty("app.contact.email"));
                });
                definition.withDefinitionProcessor(JsonNode::toPrettyString);
            });
        }));
        config.registerPlugin(new SwaggerPlugin(plugin -> {
            plugin.setDocumentationPath(documentationPath);
            plugin.setUiPath("/docs");
        }));
        config.registerPlugin(new ReDocPlugin(plugin -> {
            plugin.setDocumentationPath(documentationPath);
            plugin.setUiPath("/redoc");
        }));
    }

}
