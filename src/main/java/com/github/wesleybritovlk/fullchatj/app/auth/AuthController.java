package com.github.wesleybritovlk.fullchatj.app.auth;

import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthRequest;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthResponse;
import com.github.wesleybritovlk.fullchatj.core.CommonResource;
import com.google.inject.Inject;

import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiContentProperty;
import io.javalin.openapi.OpenApiRequestBody;
import io.javalin.openapi.OpenApiResponse;
import io.javalin.openapi.OpenApiSecurity;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

public interface AuthController {
        @OpenApi(path = "/api/v1/auth/login", methods = {
                        HttpMethod.POST }, summary = "Login", description = "Login with email and password", tags = {
                                        "Auth" }, requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = AuthRequest.Login.class)), responses = {
                                                        @OpenApiResponse(status = "200", content = {
                                                                        @OpenApiContent(type = "object", properties = {
                                                                                        @OpenApiContentProperty(name = "message", type = "string"),
                                                                                        @OpenApiContentProperty(name = "data", type = "object", from = AuthResponse.class)
                                                                        }) }) })
        public void login(Context ctx);

        @OpenApi(path = "/api/v1/auth/register", methods = {
                        HttpMethod.POST }, summary = "Register", description = "Register a new user and return a token", tags = {
                                        "Auth" }, requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = AuthRequest.Register.class)), responses = {
                                                        @OpenApiResponse(status = "201", content = {
                                                                        @OpenApiContent(type = "object", properties = {
                                                                                        @OpenApiContentProperty(name = "message", type = "string"),
                                                                                        @OpenApiContentProperty(name = "data", type = "object", from = AuthResponse.class)
                                                                        }) }) })
        public void register(Context ctx);

        @OpenApi(path = "/api/v1/auth/refresh_token", methods = {
                        HttpMethod.GET }, summary = "Refresh token", description = "Refreshs the user token", tags = {
                                        "Auth" }, responses = {
                                                        @OpenApiResponse(status = "200", content = {
                                                                        @OpenApiContent(type = "object", properties = {
                                                                                        @OpenApiContentProperty(name = "data", type = "object", from = AuthResponse.class)
                                                                        }) })
                                        }, security = @OpenApiSecurity(name = "BearerAuth"))
        public void refreshToken(Context ctx);
}

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class AuthControllerImpl implements AuthController {
        private final AuthService service;

        public void login(Context ctx) {
                val request = ctx.bodyAsClass(AuthRequest.Login.class);
                log.info("Login request received: {}", request);
                val response = service.login(request);
                val resource = CommonResource.toMessage("User logged successful.", response);
                ctx.json(resource);
        }

        public void register(Context ctx) {
                val request = ctx.bodyAsClass(AuthRequest.Register.class);
                log.info("Register request received: {}", request);
                val response = service.register(request);
                val resource = CommonResource.toMessage("User registered successful.", response);
                ctx.status(201).json(resource);
        }

        public void refreshToken(Context ctx) {
                val authorization = ctx.header("Authorization");
                log.info("Refresh token request received: {}", authorization);
                val response = service.refreshToken(authorization);
                val resource = CommonResource.toData(response);
                ctx.json(resource);
        }

}