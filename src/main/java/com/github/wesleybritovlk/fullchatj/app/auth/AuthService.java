package com.github.wesleybritovlk.fullchatj.app.auth;

import java.util.List;

import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthPayload;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthRequest;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthRequest.Login;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthRequest.Register;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthResponse;
import com.github.wesleybritovlk.fullchatj.infra.util.jwt.JwtProvide;
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
    private final JwtProvide jwtProvide;

    @Override
    public AuthResponse login(Login request) {
        log.info("User login attempt with email: {}", request.email());
        var user = AuthPayload.User.builder()
                .email(request.email())
                .scopes(List.of(AuthEnum.Scope.USER))
                .build();
        return jwtProvide.createToken(user);
    }

    @Override
    public AuthResponse register(Register request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

    @Override
    public AuthResponse refreshToken(String authorization) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshToken'");
    }

}