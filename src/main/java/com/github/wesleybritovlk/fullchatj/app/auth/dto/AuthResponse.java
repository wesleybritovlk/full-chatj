package com.github.wesleybritovlk.fullchatj.app.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.javalin.openapi.OpenApiExample;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AuthResponse(
                @OpenApiExample(value = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String accessToken,
                @OpenApiExample(value = "3600") Long expiresIn) {
}
