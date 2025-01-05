package com.github.wesleybritovlk.fullchatj.infra;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import io.javalin.config.JavalinConfig;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class OpenApiConfig {
    private final AppConfig appConfig;

    public void configure(JavalinConfig config) {
        var documentationPath = "/docs/api.json";
        config.registerPlugin(new OpenApiPlugin(plugin -> {
            plugin.withDocumentationPath(documentationPath);
            plugin.withDefinitionConfiguration((version, definition) -> {
                definition.withInfo(info -> {
                    info.title(appConfig.getProperty("app.name"));
                    info.description(appConfig.getProperty("app.description"));
                    info.version(appConfig.getProperty("app.version"));
                    info.contact(
                            appConfig.getProperty("app.contact.name"),
                            appConfig.getProperty("app.contact.url"),
                            appConfig.getProperty("app.contact.email"));
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
