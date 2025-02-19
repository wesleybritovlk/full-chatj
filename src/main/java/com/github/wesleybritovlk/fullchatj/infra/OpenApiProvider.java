package com.github.wesleybritovlk.fullchatj.infra;

import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import io.javalin.config.JavalinConfig;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import lombok.RequiredArgsConstructor;
import lombok.val;

public interface OpenApiProvider {
    void setup(JavalinConfig config);
}

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class OpenApiProviderImpl implements OpenApiProvider {
    private final Properties properties;

    public void setup(JavalinConfig config) {
        val documentationPath = "/docs/api.json";
        config.registerPlugin(new OpenApiPlugin(plugin -> {
            plugin.withDocumentationPath(documentationPath);
            plugin.withDefinitionConfiguration((version, definition) -> {
                definition.withInfo(info -> {
                    info.title(properties.getProperty("app.name"));
                    info.description(properties.getProperty("app.description"));
                    info.version(properties.getProperty("app.version"));
                    info.contact(
                            properties.getProperty("app.contact.name"),
                            properties.getProperty("app.contact.url"),
                            properties.getProperty("app.contact.email"));
                });
                definition.withDefinitionProcessor(JsonNode::toPrettyString);
                definition.withSecurity(securityConfig -> {
                    securityConfig.withBearerAuth("BearerAuth");
                    securityConfig.withGlobalSecurity("BearerAuth");
                });
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
