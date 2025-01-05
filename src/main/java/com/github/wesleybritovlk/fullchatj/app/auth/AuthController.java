package com.github.wesleybritovlk.fullchatj.app.auth;

import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthRequest;
import com.github.wesleybritovlk.fullchatj.core.CommonResource;
import com.google.inject.Inject;

import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiRequestBody;
import io.javalin.openapi.OpenApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public interface AuthController {
    @OpenApi(
        tags = { "Auth" },
        path = "/api/v1/auth/login",
        methods = {HttpMethod.POST},
        summary = "Login",
        description = "Login with email and password",
        requestBody = @OpenApiRequestBody( content = @OpenApiContent(from = AuthRequest.Login.class)),
        responses = { @OpenApiResponse(status = "200", content = @OpenApiContent(from = CommonResource.class)) }
    )
    public void login(Context ctx);

    @OpenApi(
        tags = { "Auth" },
        path = "/api/v1/auth/register",
        methods = {HttpMethod.POST},
        summary = "Register",
        description = "Register a new user and return a token",
        requestBody = @OpenApiRequestBody( content = @OpenApiContent(from = AuthRequest.Register.class)),
        responses = { @OpenApiResponse(status = "201", content = @OpenApiContent(from = CommonResource.class)) }
    )
    public void register(Context ctx);

    @OpenApi(
        tags = { "Auth" },
        path = "/api/v1/auth/refresh_token",
        summary = "Refresh token",
        description = "Refreshs the user token",
        headers = { @OpenApiParam(name = "Authorization", description = "Bearer token", required = true) },
        responses = { @OpenApiResponse(status = "200", content = @OpenApiContent(from = CommonResource.class)) }
    )
    public void refreshToken(Context ctx);
}

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class AuthControllerImpl implements AuthController {
    private final AuthService service;

    public void login(Context ctx) {
        var request = ctx.bodyAsClass(AuthRequest.Login.class);
        log.info("Login request received: {}", request);
        var response = service.login(request);
        var resource = CommonResource.toMessage("User logged successful.", response);
        ctx.json(resource);
    }

    public void register(Context ctx) {
        var request = ctx.bodyAsClass(AuthRequest.Register.class);
        log.info("Register request received: {}", request);
        var response = service.register(request);
        var resource = CommonResource.toMessage("User registered successful.", response);
        ctx.status(201).json(resource);
    }

    public void refreshToken(Context ctx) {
        var authorization = ctx.header("Authorization");
        log.info("Refresh token request received: {}", authorization);
        var response = service.refreshToken(authorization);
        var resource = CommonResource.toMessage("Token refreshed successful.", response);
        ctx.json(resource);
    }

}