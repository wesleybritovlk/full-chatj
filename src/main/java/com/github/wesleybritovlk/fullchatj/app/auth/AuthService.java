package com.github.wesleybritovlk.fullchatj.app.auth;

import java.util.List;

import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthPayload;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthRequest;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthRequest.Login;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthRequest.Register;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthResponse;
import com.github.wesleybritovlk.fullchatj.app.auth.entity.AuthUserEntity;
import com.github.wesleybritovlk.fullchatj.infra.util.JwtProvider;
import com.github.wesleybritovlk.fullchatj.infra.util.PasswordEncoderProvider;
import com.github.wesleybritovlk.fullchatj.infra.util.PasswordEncoderProvider.EncoderType;
import com.google.inject.Inject;

import io.javalin.http.UnauthorizedResponse;
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
    private final PasswordEncoderProvider passEncoder = PasswordEncoderProvider.of(EncoderType.BCRYPT);
    private final PasswordEncoderProvider fakePassEncoder = PasswordEncoderProvider.of(EncoderType.SCRYPT);

    @Override
    public AuthResponse login(Login request) {
        var email = request.email();
        log.info("User login attempt with email: {}", email);
        boolean match = false;
        var entity = repository.findByEmail(email).orElse(null);
        match = entity != null;
        if (match) {
            fakePassEncoder.matches(request.password(), entity.getPassword());
            match = passEncoder.matches(request.password(), entity.getPassword());
        }
        if (!match) {
            log.error("User not found with email: {}", email);
            throw new UnauthorizedResponse("Invalid email or password");
        }
        var authUser = AuthPayload.User.builder()
                .email(request.email())
                .scopes(List.of(AuthEnum.Scope.USER))
                .build();
        return jwtProvider.createToken(authUser);
    }

    @Override
    public AuthResponse register(Register request) {
        log.info("User register attempt with email: {}", request.email());
        if (repository.existsByEmail(request.email())) {
            log.error("User already registered with email: {}", request.email());
            throw new UnauthorizedResponse("User already registered");
        }
        fakePassEncoder.encode(request.password());
        var passwordEncoded = passEncoder.encode(request.password());
        repository.save(AuthUserEntity.builder()
                .email(request.email())
                .password(passwordEncoded)
                .build());
        var user = AuthPayload.User.builder()
                .email(request.email())
                .scopes(List.of(AuthEnum.Scope.USER))
                .build();
        return jwtProvider.createToken(user);
    }

    @Override
    public AuthResponse refreshToken(String authorization) {
        var subject = jwtProvider.getPayloadValid(authorization).subject();
        log.info("User refresh token attempt with email: {}", subject);
        if (!repository.existsByEmail(subject)) {
            log.error("User not found with email: {}", subject);
            throw new UnauthorizedResponse("User not registered");
        }
        var user = AuthPayload.User.builder()
                .email(subject)
                .scopes(List.of(AuthEnum.Scope.USER))
                .build();
        return jwtProvider.createToken(user);
    }

}