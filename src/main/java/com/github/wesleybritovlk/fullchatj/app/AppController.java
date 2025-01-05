package com.github.wesleybritovlk.fullchatj.app;

import java.util.Properties;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.inject.Inject;

import io.javalin.http.Context;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

public interface AppController {

        @OpenApi(tags = {
                        "App" }, path = "/", summary = "Get app information", description = "Returns general information about the app", responses = {
                                        @OpenApiResponse(status = "200", content = @OpenApiContent(from = Response.class))
                        })
        void get(Context ctx);

}

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class AppControllerImpl implements AppController {
        private final Properties properites;

        public void get(Context ctx) {
                var currentUrl = "%s%s".formatted(ctx.url(), properites.getProperty("app.docsPath"));
                var response = Response.builder()
                                .name(properites.getProperty("app.name"))
                                .version(properites.getProperty("app.version"))
                                .description(properites.getProperty("app.description"))
                                .documentation(currentUrl)
                                .build();
                ctx.json(response);
        }
}

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
record Response(
                String name,
                String version,
                String description,
                String documentation) {
}