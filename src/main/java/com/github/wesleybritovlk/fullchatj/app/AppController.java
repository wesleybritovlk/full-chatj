package com.github.wesleybritovlk.fullchatj.app;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.wesleybritovlk.fullchatj.infra.AppConfig;

import io.javalin.http.Context;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiResponse;
import lombok.Builder;
import lombok.Getter;

public class AppController {
    @OpenApi(tags = {
            "App" }, path = "/", summary = "Get app information", description = "Returns general information about the app", responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = Response.class))
            })
    public void get(Context ctx) {
        var currentUrl = "%s%s".formatted(ctx.url(), AppConfig.getProperty("app.docsPath"));
        var response = Response.builder()
                .name(AppConfig.getProperty("app.name"))
                .version(AppConfig.getProperty("app.version"))
                .description(AppConfig.getProperty("app.description"))
                .documentation(currentUrl)
                .build();
        ctx.json(response);
    }
}

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class Response {
    private String name;
    private String version;
    private String description;
    private String documentation;
}