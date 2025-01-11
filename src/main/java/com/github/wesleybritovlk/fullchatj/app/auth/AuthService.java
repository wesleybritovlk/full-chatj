package com.github.wesleybritovlk.fullchatj.app.auth;

import java.util.List;

import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthPayload;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthRequest;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthRequest.Login;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthRequest.Register;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthResponse;
import com.github.wesleybritovlk.fullchatj.infra.util.JwtProvider;
import com.google.inject.Inject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public interface AuthService {
    AuthResponse login(AuthRequest.Login request);

    AuthResponse register(AuthRequest.Register request);

    AuthResponse refreshToken(String authorization);
}

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;
    private final AuthRepository repository;

    @Override
    public AuthResponse login(Login request) {
        log.info("User login attempt with email: {}", request.email());
        var authUser = AuthPayload.User.builder()
                .email(request.email())
                .scopes(List.of(AuthEnum.Scope.USER))
                .build();
        return jwtProvider.createToken(authUser);
    }

    @Override
    public AuthResponse register(Register request) {
        var user = AuthPayload.User.builder()
                .email(request.email())
                .scopes(List.of(AuthEnum.Scope.USER))
                .build();
        return jwtProvider.createToken(user);
    }

    @Override
    public AuthResponse refreshToken(String authorization) {
        var subject = jwtProvider.getPayloadValid(authorization).subject();
        var user = AuthPayload.User.builder()
                .email(subject)
                .scopes(List.of(AuthEnum.Scope.USER))
                .build();
        return jwtProvider.createToken(user);
    }

}