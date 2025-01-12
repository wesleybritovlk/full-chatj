package com.github.wesleybritovlk.fullchatj.infra.util;

import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthPayload;
import com.github.wesleybritovlk.fullchatj.app.auth.dto.AuthResponse;
import com.google.inject.Inject;

import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

public interface JwtProvider {
    AuthResponse createToken(AuthPayload.User payload);

    boolean isExpired(String token);

    AuthResponse getAuthResponse(String authorization);

    JwtPayload getPayload(String token);

    JwtPayload getPayloadValid(String authorization);

    @Builder
    record JwtPayload(
            String subject,
            Date expiration) {
    }
}

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class JwtProviderImpl implements JwtProvider {
    private final Jwt jwt;
    private final Properties properties;

    private static final String TOKEN_PREFIX = "Bearer ";

    public AuthResponse createToken(AuthPayload.User request) {
        var claims = Map.of("scope", (Object) request.scopes());
        var token = jwt.buildToken(claims, request.email());
        var expiration = Long.parseLong(properties.getProperty("jwt.expiration"));
        return AuthResponse.builder()
                .accessToken(token)
                .expiresIn(expiration)
                .build();
    }

    @Override
    public boolean isExpired(String token) {
        if (token == null)
            return true;
        try {
            return jwt.getClaim(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public AuthResponse getAuthResponse(String authorization) {
        var token = authorization != null ? authorization.substring(TOKEN_PREFIX.length()) : null;
        if (!isExpired(token)) {
            var expiration = jwt.getClaim(token, Claims::getExpiration).toInstant();
            return AuthResponse.builder()
                    .accessToken(token)
                    .expiresIn(expiration.toEpochMilli())
                    .build();
        }
        return AuthResponse.builder().build();
    }

    @Override
    public JwtPayload getPayload(String token) {
        var subject = jwt.getClaim(token, Claims::getSubject);
        var expiration = jwt.getClaim(token, Claims::getExpiration);
        return JwtPayload.builder()
                .subject(subject)
                .expiration(expiration)
                .build();
    }

    @Override
    public JwtPayload getPayloadValid(String authorization) {
        var token = this.getAuthResponse(authorization).accessToken();
        if (token == null || token.isBlank() || isExpired(token))
            throw new UnauthorizedResponse();
        return this.getPayload(token);
    }

}

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class Jwt {
    private final Properties properties;

    private SecretKey secretKey() {
        var secret = properties.getProperty("jwt.secret");
        var base64 = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(base64);
    }

    private Claims extractClaims(String token) {
        var jwtParser = Jwts.parser().verifyWith(secretKey()).build();
        var signedClaims = jwtParser.parseSignedClaims(token);
        return signedClaims.getPayload();
    }

    protected <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        var claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    protected String buildToken(Map<String, Object> claims, String subject) {
        var expiration = Long.parseLong(properties.getProperty("jwt.expiration"));
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(secretKey())
                .compact();
    }
}
